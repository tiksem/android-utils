package com.utilsframework.android.threading;

import android.os.AsyncTask;
import android.os.Handler;

import java.util.*;

/**
 * Created by Tikhonenko.S on 27.09.13.
 */
public class Tasks {
    public static <Result> void executeSequentially(final Iterable<Task<Result>> tasks,
                                                    final OnFinish<List<Result>> onFinish){
        new AsyncTask<Void,Void,List<Result>>(){
            @Override
            protected List<Result> doInBackground(Void... voids) {
                List<Result> results = new ArrayList<Result>();
                for(Task<Result> task : tasks){
                    Result result = task.execute();
                    results.add(result);
                }
                return results;
            }

            @Override
            protected void onPostExecute(List<Result> results) {
                onFinish.onFinish(results);
            }
        };
    }

    public static <SuccessResult, ErrorResult> void executeAsyncTasks(
            final TasksCompletionCallbacks<SuccessResult, ErrorResult> tasksCompletionCallbacks,
            AsyncRequestTask<SuccessResult, ErrorResult>... tasks
    ){
        executeAsyncTasks(Arrays.<AsyncRequestTask<SuccessResult, ErrorResult>>asList(tasks), tasksCompletionCallbacks);
    }

    public static <SuccessResult, ErrorResult> void executeAsyncTasks(
            final Collection<AsyncRequestTask<SuccessResult,ErrorResult>> tasks,
            final TasksCompletionCallbacks<SuccessResult, ErrorResult> tasksCompletionCallbacks
    ){
        final int tasksCount = tasks.size();
        if(tasksCount <= 0){
            tasksCompletionCallbacks.onAllComplete(
                    Collections.<TasksCompletionCallbacks.Result<SuccessResult, ErrorResult>>
                            emptyList());
            return;
        }

        final List<TasksCompletionCallbacks.Result<SuccessResult, ErrorResult>> results =
                new ArrayList<TasksCompletionCallbacks.Result<SuccessResult, ErrorResult>>(tasksCount);

        for(final AsyncRequestTask<SuccessResult, ErrorResult> request : tasks){
            request.execute(new OnTaskCompleted<SuccessResult, ErrorResult>() {
                void checkForCompletion(){
                    int completedTasksCount = results.size();
                    if(completedTasksCount >= tasksCount){
                        tasksCompletionCallbacks.onAllComplete(results);
                    }
                }

                @Override
                public void onError(final ErrorResult errorResult) {
                    if(errorResult == null){
                        throw new IllegalArgumentException("errorResult cannot be null");
                    }

                    TasksCompletionCallbacks.Result<SuccessResult, ErrorResult> result =
                            new TasksCompletionCallbacks.Result<SuccessResult, ErrorResult>() {
                        @Override
                        public ErrorResult getError() {
                            return errorResult;
                        }

                        @Override
                        public SuccessResult getSuccessResult() {
                            return null;
                        }

                        @Override
                        public AsyncRequestTask<SuccessResult, ErrorResult> getTask() {
                            return request;
                        }
                    };

                    results.add(result);
                    tasksCompletionCallbacks.onComplete(result);
                    checkForCompletion();
                }

                @Override
                public void onSuccess(final SuccessResult successResult) {
                    TasksCompletionCallbacks.Result<SuccessResult, ErrorResult> result =
                            new TasksCompletionCallbacks.Result<SuccessResult, ErrorResult>() {
                                @Override
                                public ErrorResult getError() {
                                    return null;
                                }

                                @Override
                                public SuccessResult getSuccessResult() {
                                    return successResult;
                                }

                                @Override
                                public AsyncRequestTask<SuccessResult, ErrorResult> getTask() {
                                    return request;
                                }
                            };

                    results.add(result);
                    tasksCompletionCallbacks.onComplete(result);
                    checkForCompletion();
                }
            });
        }
    }

    public static <SuccessResult,ErrorResult> AsyncRequestTask<SuccessResult,ErrorResult>
    combine(final Collection<AsyncRequestTask<SuccessResult,ErrorResult>> tasks){
        return new AsyncRequestTask<SuccessResult, ErrorResult>() {
            @Override
            public void execute(final OnTaskCompleted<SuccessResult, ErrorResult> onRequestCompleted) {
                executeAsyncTasks(tasks, new TasksCompletionCallbacks<SuccessResult, ErrorResult>() {
                    boolean errorOccurred = false;

                    @Override
                    public void onAllComplete(List<Result<SuccessResult, ErrorResult>> results) {
                        if(!errorOccurred){
                            onRequestCompleted.onSuccess(
                                    results.get(results.size() - 1).getSuccessResult());
                        }
                    }

                    @Override
                    public void onComplete(Result<SuccessResult, ErrorResult> result) {
                        ErrorResult error = result.getError();
                        if(error != null){
                            onRequestCompleted.onError(error);
                            errorOccurred = true;
                        }
                    }
                });
            }
        };
    }

    public static <T>
    BackgroundLoopEvent waitForResult(final ResultLoop<T> resultLoop){
        final Handler handler = new Handler();

        final BackgroundLoopEvent backgroundLoopEvent = new BackgroundLoopEvent();
        backgroundLoopEvent.setOnStop(new BackgroundLoopEvent.OnStop() {
            @Override
            public void onStop(boolean timeIsUp) {
                resultLoop.onTimeIsUp();
            }
        });

        backgroundLoopEvent.run(new Runnable() {
            @Override
            public void run() {
                if (resultLoop.resultIsReady()) {
                    backgroundLoopEvent.setOnStop(new BackgroundLoopEvent.OnStop() {
                        @Override
                        public void onStop(boolean timeIsUp) {
                            final T result = resultLoop.getResult();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    resultLoop.handleResult(result);
                                }
                            });
                        }
                    });
                    backgroundLoopEvent.stop();
                }
            }
        });

        return backgroundLoopEvent;
    }

    public static void executeRunnableQueue(Iterable<Runnable> runnables){
        for(Runnable runnable : runnables){
            runnable.run();
        }
    }

    public static void executeAndClearQueue(Collection<Runnable> runnables){
        executeRunnableQueue(runnables);
        runnables.clear();
    }
}
