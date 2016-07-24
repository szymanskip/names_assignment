package pl.szymanskip.names;

import android.app.Application;

import pl.szymanskip.names.di.AndroidComponent;
import pl.szymanskip.names.di.AndroidModule;
import pl.szymanskip.names.di.DaggerAndroidComponent;

public class NamesApplication extends Application {

  private AndroidComponent androidComponent;

  @Override
  public void onCreate() {
    super.onCreate();
    androidComponent = DaggerAndroidComponent.builder()
        .androidModule(new AndroidModule(this))
        .build();
  }

  public AndroidComponent getAndroidComponent() {
    return androidComponent;
  }
}
