package pl.szymanskip.names.domain.repository;

import pl.szymanskip.names.domain.Name;
import pl.szymanskip.names.domain.Region;
import rx.Single;

public interface NamesRepository {

  Single<Name> getName(Region region);
}
