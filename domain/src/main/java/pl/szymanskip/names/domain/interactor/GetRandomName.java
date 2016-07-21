package pl.szymanskip.names.domain.interactor;

import pl.szymanskip.names.domain.FullName;
import pl.szymanskip.names.domain.Region;
import pl.szymanskip.names.domain.repository.NamesRepository;
import rx.Single;

public class GetRandomName implements UseCase<Region, Single<FullName>> {
  private final NamesRepository namesRepository;

  public GetRandomName(NamesRepository namesRepository) {
    this.namesRepository = namesRepository;
  }

  @Override
  public Single<FullName> execute(Region region) {
    return namesRepository.getName(region);
  }
}
