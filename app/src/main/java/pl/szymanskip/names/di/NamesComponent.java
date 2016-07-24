package pl.szymanskip.names.di;

import dagger.Component;
import pl.szymanskip.names.RandomNameActivity;

@SuppressWarnings("WeakerAccess")
@ViewScope
@Component(modules = NamesModule.class, dependencies = AndroidComponent.class)
public interface NamesComponent {
  void inject(RandomNameActivity randomNameActivity);
}
