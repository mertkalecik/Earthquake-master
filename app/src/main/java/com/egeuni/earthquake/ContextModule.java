package com.egeuni.earthquake;

import android.content.Context;
import javax.inject.Named;
import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {
    Context context;

    ContextModule(Context context) {
        this.context = context;
    }


    @Provides
    Context getContext() {
        return context;
    }
}
