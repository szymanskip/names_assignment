package pl.szymanskip.names.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import pl.szymanskip.names.data.DeviceRegionProvider;
import pl.szymanskip.names.data.RemoteNamesRepository;
import pl.szymanskip.names.data.remote.UiNamesService;
import pl.szymanskip.names.domain.RegionProvider;
import pl.szymanskip.names.domain.interactor.GetCurrentRegion;
import pl.szymanskip.names.domain.interactor.GetRandomName;
import pl.szymanskip.names.domain.repository.NamesRepository;
import retrofit2.Retrofit;

@Module
class NamesModule {

  @Provides
  @ViewScope
  GetRandomName providesGetRandomName(Retrofit retrofit) {
    UiNamesService uiNamesService = retrofit.create(UiNamesService.class);
    NamesRepository namesRepository = new RemoteNamesRepository(uiNamesService);
    return new GetRandomName(namesRepository);
  }

  @Provides
  @ViewScope
  GetCurrentRegion provideGetCurrentRegion(Context context) {
    RegionProvider regionProvider = new DeviceRegionProvider(context);
    return new GetCurrentRegion(regionProvider);
  }
}
