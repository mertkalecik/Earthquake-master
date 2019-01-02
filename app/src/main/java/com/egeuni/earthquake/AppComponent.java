package com.egeuni.earthquake;

import dagger.Component;

@Component(modules = {ContextModule.class})
public interface AppComponent {
    void inject(PersonDataAdapter personDataAdapter);
    void inject(EarthquakeDataAdapter earthquakeDataAdapter);
    void inject(Utils utils);
}
