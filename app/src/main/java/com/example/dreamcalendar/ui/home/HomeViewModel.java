package com.example.dreamcalendar.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Dodawanie snów z kategorią po dacie\nDodawanie własnych kategorii snów\nWyszukiwanie słów kluczowych w snach");
    }

    public LiveData<String> getText() {
        return mText;
    }
}