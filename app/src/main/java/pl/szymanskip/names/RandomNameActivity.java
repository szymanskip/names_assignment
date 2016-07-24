package pl.szymanskip.names;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.szymanskip.names.di.AndroidComponent;
import pl.szymanskip.names.di.DaggerNamesComponent;
import pl.szymanskip.names.domain.ErrorHandler;
import pl.szymanskip.names.domain.interactor.GetCurrentRegion;
import pl.szymanskip.names.domain.interactor.GetRandomName;

import static pl.szymanskip.names.R.id.region;

public class RandomNameActivity extends AppCompatActivity implements RandomNameView {
  private static final int REQUEST_PERMISSION_LOCATION = 967;

  @BindView(region) TextView regionView;
  @BindView(R.id.name) TextView nameView;
  @BindView(R.id.button_get_name) Button getNameButton;
  @Inject GetCurrentRegion getCurrentRegion;
  @Inject GetRandomName getRandomName;
  @Inject ErrorHandler errorHandler;

  private RandomNamePresenter presenter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_random_name);
    ButterKnife.bind(this);

    AndroidComponent androidComponent = ((NamesApplication) getApplication()).getAndroidComponent();
    DaggerNamesComponent.builder()
        .androidComponent(androidComponent)
        .build()
        .inject(this);

    presenter = new RandomNamePresenter(getCurrentRegion, getRandomName, errorHandler);

    checkLocationPermissions();
  }

  private void checkLocationPermissions() {
    int fineLocationPermission = ActivityCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION);
    int coarseLocationPermission = ActivityCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_COARSE_LOCATION);
    if (fineLocationPermission != PackageManager.PERMISSION_GRANTED
        && coarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
          new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
              Manifest.permission.ACCESS_COARSE_LOCATION }, REQUEST_PERMISSION_LOCATION);
    } else {
      presenter.loadLocation();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (requestCode == REQUEST_PERMISSION_LOCATION) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        presenter.loadLocation();
      } else {
        new AlertDialog.Builder(this)
            .setMessage(getString(R.string.message_requires_permission_dialog))
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                checkLocationPermissions();
              }
            })
            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                finish();
              }
            })
            .setCancelable(false)
            .show();
      }
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  @OnClick(R.id.button_get_name)
  void onGetNameClicked() {
    presenter.loadRandomName();
  }

  @Override
  protected void onStart() {
    super.onStart();
    presenter.attach(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    presenter.detach();
  }

  @Override
  public void showWaitingForLocation() {
    regionView.setText(R.string.waiting_for_location);
    getNameButton.setEnabled(false);
  }

  @Override
  public void showLocation(RegionViewModel region) {
    getNameButton.setEnabled(true);
    regionView.setText(getString(R.string.current_location, region.getName()));
  }

  @Override
  public void showProgress() {
    nameView.setText(R.string.loading_name);
    getNameButton.setEnabled(false);
  }

  @Override
  public void hideProgress() {
    nameView.setText("");
    getNameButton.setEnabled(true);
  }

  @Override
  public void showRandomName(NameViewModel nameViewModel) {
    nameView.setText(nameViewModel.getFullName());
    getNameButton.setEnabled(true);
  }

  @Override
  public void showError(String message) {
    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    getNameButton.setEnabled(true);
  }

  @Override
  public void showSpecialName(String firstName) {
    //noinspection deprecation
    new AlertDialog.Builder(this)
        .setTitle(R.string.dialog_title)
        .setMessage(Html.fromHtml(getString(R.string.dialog_message, firstName)))
        .setPositiveButton(android.R.string.ok, null)
        .setCancelable(true)
        .show();
  }
}
