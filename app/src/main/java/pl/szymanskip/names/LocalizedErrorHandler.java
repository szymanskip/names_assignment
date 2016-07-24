package pl.szymanskip.names;

import android.content.res.Resources;

import java.net.HttpURLConnection;
import java.net.UnknownHostException;

import pl.szymanskip.names.domain.ErrorHandler;
import retrofit2.adapter.rxjava.HttpException;

public class LocalizedErrorHandler implements ErrorHandler {
  private final Resources resources;

  public LocalizedErrorHandler(Resources resources) {
    this.resources = resources;
  }

  @Override
  public String handle(Throwable throwable) {
    return localize(throwable);
  }

  private String localize(Throwable throwable) {
    if (throwable instanceof UnknownHostException) {
      return resources.getString(R.string.no_internet);
    } else if (throwable instanceof HttpException) {
      HttpException httpException = (HttpException) throwable;
      if (httpException.code() == HttpURLConnection.HTTP_UNAVAILABLE) {
        return resources.getString(R.string.not_available);
      }
      return httpException.getMessage();
    }
    return throwable.getMessage();
  }

}
