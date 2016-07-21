package pl.szymanskip.names.domain.repository;

import pl.szymanskip.names.domain.FullName;
import rx.Single;

public interface NamesRepository {

  Single<FullName> getName(String region);
}
