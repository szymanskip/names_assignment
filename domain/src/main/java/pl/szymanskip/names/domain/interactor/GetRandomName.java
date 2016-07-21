package pl.szymanskip.names.domain.interactor;

import pl.szymanskip.names.domain.FullName;
import pl.szymanskip.names.domain.repository.NamesRepository;
import rx.Single;

public class GetRandomName implements UseCase<String, Single<FullName>> {
  private final NamesRepository namesRepository;

  public GetRandomName(NamesRepository namesRepository) {
    this.namesRepository = namesRepository;
  }

  @Override
  public Single<FullName> execute(String region) {
    return namesRepository.getName(region);
  }
}
