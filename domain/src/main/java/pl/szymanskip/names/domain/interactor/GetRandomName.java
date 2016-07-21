package pl.szymanskip.names.domain.interactor;

import pl.szymanskip.names.domain.Name;
import pl.szymanskip.names.domain.Region;
import pl.szymanskip.names.domain.repository.NamesRepository;
import rx.Single;

public class GetRandomName implements UseCase<Region, Single<Name>> {
  private final NamesRepository namesRepository;

  public GetRandomName(NamesRepository namesRepository) {
    this.namesRepository = namesRepository;
  }

  @Override
  public Single<Name> execute(Region region) {
    return namesRepository.getName(region);
  }
}
