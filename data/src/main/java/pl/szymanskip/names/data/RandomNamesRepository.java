package pl.szymanskip.names.data;

import pl.szymanskip.names.data.remote.NameWs;
import pl.szymanskip.names.data.remote.UiNamesService;
import pl.szymanskip.names.domain.FullName;
import pl.szymanskip.names.domain.repository.NamesRepository;
import rx.Single;
import rx.functions.Func1;

public class RandomNamesRepository implements NamesRepository {

  private final UiNamesService uiNamesService;

  public RandomNamesRepository(UiNamesService uiNamesService) {
    this.uiNamesService = uiNamesService;
  }

  @Override
  public Single<FullName> getName(String region) {
    return uiNamesService.getName(region)
        .map(new Func1<NameWs, FullName>() {
          @Override
          public FullName call(NameWs nameWs) {
            return mapWsObject(nameWs);
          }
        });
  }

  private FullName mapWsObject(NameWs nameWs) {
    return new FullName(nameWs.getName(), nameWs.getSurname());
  }
}
