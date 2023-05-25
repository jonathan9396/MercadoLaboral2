package com.example.mercadolaboral2.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

public class RemoveLiveDataObserver {
    public static <T> void observeOnce(final LiveData<T> liveData, final Observer<T> observer) {
        liveData.observeForever(new Observer<T>() {
            @Override
            public void onChanged(T t) {
                liveData.removeObserver(this);
                observer.onChanged(t);
            }
        });
    }
}
