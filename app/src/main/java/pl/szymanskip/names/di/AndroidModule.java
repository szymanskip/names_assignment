package pl.szymanskip.names.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AndroidModule {
  private Context context;

  public AndroidModule(Context context) {
    this.context = context.getApplicationContext();
  }

  @Provides
  @Singleton
  Context provideApplicationContext() {
    return context;
  }
}
