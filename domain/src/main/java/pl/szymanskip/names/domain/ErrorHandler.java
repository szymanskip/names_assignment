package pl.szymanskip.names.domain;

public interface ErrorHandler {
  String handle(Throwable throwable);
}