package pl.szymanskip.names.domain.repository;

import pl.szymanskip.names.domain.FullName;
import pl.szymanskip.names.domain.Region;
import rx.Single;

public interface NamesRepository {

  Single<FullName> getName(Region region);
}
