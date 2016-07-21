package pl.szymanskip.names.domain.interactor;

public interface UseCase<Input, Output> {

  Output execute(Input param);
}
