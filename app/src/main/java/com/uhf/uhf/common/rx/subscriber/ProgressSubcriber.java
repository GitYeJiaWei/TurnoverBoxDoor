package com.uhf.uhf.common.rx.subscriber;

import android.content.Context;

import com.uhf.uhf.common.exception.BaseException;
import com.uhf.uhf.ui.BaseView;

import java.io.IOException;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public  abstract  class ProgressSubcriber<T> extends ErrorHandlerSubscriber<T>  {

    private BaseView mView;

    public ProgressSubcriber(Context context, BaseView view) {
        super(context);
        this.mView = view;
    }

    public boolean isShowProgress(){
        return true;
    }


    @Override
    public void onSubscribe(Disposable d) {
        if(isShowProgress()){
            mView.showLoading();
        }
    }

    @Override
    public void onComplete() {
        //当所有onNext()完成后触发
        mView.dismissLoading();
    }

    @Override
    public void onError(Throwable e) {
        //当出现错误时触发
        if (e instanceof HttpException){
            ResponseBody responseBody = ((HttpException) e).response().errorBody();
            if (responseBody != null){
                try {
                    responseBody.string();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        e.printStackTrace();
        BaseException baseException =  mErrorHandler.handleError(e);
        mView.showError(baseException.getDisplayMessage());
    }

}
