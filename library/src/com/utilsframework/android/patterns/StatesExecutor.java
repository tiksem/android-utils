package com.utilsframework.android.patterns;

import com.utilsframework.android.threading.Tasks;

import java.util.*;

/**
 * User: Tikhonenko.S
 * Date: 17.07.14
 * Time: 19:05
 */
public class StatesExecutor<State> {
    private Map<State, Set<Runnable>> tasksByStates = new HashMap<State, Set<Runnable>>();
    private State currentState;

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        if(currentState == null){
            throw new NullPointerException();
        }

        if(this.currentState == currentState){
            return;
        }

        this.currentState = currentState;
        Set<Runnable> tasks = tasksByStates.get(currentState);
        if (tasks != null) {
            ArrayList<Runnable> tasksCopy = new ArrayList<Runnable>(tasks);
            tasks.clear();
            Tasks.executeRunnableQueue(tasksCopy);
            for(Set<Runnable> tasksOfState : tasksByStates.values()){
                tasksOfState.removeAll(tasksCopy);
            }
        }
    }

    public StatesExecutor(State currentState) {
        setCurrentState(currentState);
    }

    public interface Execution<State> {
        public Runnable getTask();
        public List<State> getStatesWhenTaskCanBeExecuted();
        public List<State> getStatesWhenTaskWillBeRejected();
    }

    public void execute(Execution<State> execution) {
        if(execution == null){
            throw new NullPointerException("execution == null");
        }

        if(execution.getStatesWhenTaskWillBeRejected().contains(currentState)){
            return;
        }

        Runnable task = execution.getTask();

        List<State> canBeExecutedOnStates = execution.getStatesWhenTaskCanBeExecuted();
        if(canBeExecutedOnStates.contains(currentState)){
            task.run();
            return;
        }

        for (State state : canBeExecutedOnStates) {
            Set<Runnable> tasksByState = tasksByStates.get(state);
            if(tasksByState == null){
                tasksByState = new LinkedHashSet<Runnable>();
                tasksByStates.put(state, tasksByState);
            }

            tasksByState.add(task);
        }
    }

    public void cancelTask(Runnable task) {
        for(Set<Runnable> tasks : tasksByStates.values()){
            tasks.remove(task);
        }
    }

    public void cancelAll() {
        for(Set<Runnable> tasks : tasksByStates.values()){
            tasks.clear();
        }
    }
}
