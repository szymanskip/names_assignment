package pl.szymanskip.names.data;

import pl.szymanskip.names.data.remote.NameWs;
import pl.szymanskip.names.data.remote.UiNamesService;
import pl.szymanskip.names.domain.Name;
import pl.szymanskip.names.domain.Region;
import pl.szymanskip.names.domain.repository.NamesRepository;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RemoteNamesRepository implements NamesRepository {

  private final UiNamesService uiNamesService;

  public RemoteNamesRepository(UiNamesService uiNamesService) {
    this.uiNamesService = uiNamesService;
  }

  @Override
  public Single<Name> getName(Region region) {
    return uiNamesService.getName(region.getName())
        .map(new Func1<NameWs, Name>() {
          @Override
          public Name call(NameWs nameWs) {
            return mapWsObject(nameWs);
          }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  private Name mapWsObject(NameWs nameWs) {
    return new Name(nameWs.getName(), nameWs.getSurname());
  }
}
