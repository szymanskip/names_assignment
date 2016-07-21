package pl.szymanskip.names;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.szymanskip.names.data.DeviceRegionProvider;
import pl.szymanskip.names.data.RemoteNamesRepository;
import pl.szymanskip.names.data.remote.UiNamesService;
import pl.szymanskip.names.domain.RegionProvider;
import pl.szymanskip.names.domain.interactor.GetCurrentRegion;
import pl.szymanskip.names.domain.interactor.GetRandomName;
import pl.szymanskip.names.domain.repository.NamesRepository;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static pl.szymanskip.names.R.id.region;

public class RandomNameActivity extends AppCompatActivity implements RandomNameView {
  private static final int REQUEST_PERMISSION_LOCATION = 967;

  @BindView(region) TextView regionView;
  @BindView(R.id.name) TextView nameView;
  @BindView(R.id.button_get_name) Button getNameButton;

  private RandomNamePresenter presenter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_random_name);
    ButterKnife.bind(this);

    RegionProvider regionProvider = new DeviceRegionProvider(getApplicationContext());
    GetCurrentRegion getCurrentRegion = new GetCurrentRegion(regionProvider);

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .addNetworkInterceptor(interceptor)
        .build();
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://uinames.com/")
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build();
    UiNamesService uiNamesService = retrofit.create(UiNamesService.class);
    NamesRepository namesRepository = new RemoteNamesRepository(uiNamesService);
    GetRandomName getRandomName = new GetRandomName(namesRepository);
    presenter = new RandomNamePresenter(getCurrentRegion, getRandomName);

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
            .setMessage("Application requires location permission to run.")
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
    regionView.setText("Waiting for location");
    getNameButton.setEnabled(false);
  }

  @Override
  public void showLocation(RegionViewModel region) {
    getNameButton.setEnabled(true);
    regionView.setText(region.getName());
  }

  @Override
  public void showProgress() {

  }

  @Override
  public void hideProgress() {

  }

  @Override
  public void showRandomName(NameViewModel nameViewModel) {
    nameView.setText(nameViewModel.getFullName());
  }
}
