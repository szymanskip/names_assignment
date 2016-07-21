package pl.szymanskip.names.data.remote;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

public interface UiNamesService {

  @GET("")
  Single<NameWs> getName(@Query("region") String region);
}
