package pl.szymanskip.names.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import pl.szymanskip.names.domain.ErrorHandler;
import retrofit2.Retrofit;

@Singleton
@Component(modules = { AndroidModule.class, NetworkModule.class })
public interface AndroidComponent {

  Context context();

  Retrofit retrofit();

  ErrorHandler errorHandler();
}
