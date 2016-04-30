package com.eastflag.secondproject.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.eastflag.secondproject.R;
import com.eastflag.secondproject.domain.PlaceVO;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceFragment extends Fragment {
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation; //현재위치를 담고 있는 객체
    private GoogleMap mGoogleMap;
    private LocationRequest mLocationRequest;
    private MarkerOptions currentOpt;
    private AQuery mAq;
    private final String PLACES_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyAjrXnxmyM1go9fYrxO2CgDxvAQx7_vD8A&location=%f,%f&radius=500&name=%s";
    private ArrayList<PlaceVO> mPlaceList = new ArrayList<PlaceVO>();

    @Bind(R.id.etSearch)
    EditText etSearch;

    public PlaceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place, container, false);
        ButterKnife.bind(this, view);
        mAq = new AQuery(view);

        //구글 api 엑세스
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(mConnectionCallbacks)
                    .addOnConnectionFailedListener(mOnConnectionFailedListener)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }

        mLocationRequest = new LocationRequest();
        //위치를 가져오는 주기 설정
        mLocationRequest.setInterval(1000 * 60 * 1); //1 minutes
        mLocationRequest.setFastestInterval(1000 * 60 * 10);
        //4가지 배터리 소모와 위치 정확성 설정
        //위치 정확도를 높이면 배터리 소모가 많아지고 정확도를 낮추면 배터리 소모가 낮아짐
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        return view;
    }

    @OnClick(R.id.btnSearch) void btnSearch() {
        //키보드 내리기
        InputMethodManager imm = (InputMethodManager)
                getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        mPlaceList.clear();
        displayMap();

        //EditText의 내용으로 places api 호출후 결과 파싱
        String url = String.format(PLACES_API, mLastLocation.getLatitude(),
                mLastLocation.getLongitude(), etSearch.getText().toString());
        Log.d("LDK", url);
        mAq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                try {
                    Log.d("LDK", object.toString(1));
                    //Log.d("LDK", object.toString(1));
                    //결과를 파싱해서 지도에 뿌려주기 & 리스트 뷰 만들기
                    JSONArray array = object.getJSONArray("results");
                    for (int i = 0; i < array.length(); ++i) {
                        JSONObject json = array.getJSONObject(i);
                        String name = json.getString("name");
                        String vicinity = json.getString("vicinity");
                        double lat = json.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                        double lng = json.getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                        //객체에 담기
                        PlaceVO place = new PlaceVO();
                        place.name = name;
                        place.vicinity = vicinity;
                        place.lat = lat;
                        place.lng = lng;
                        mPlaceList.add(place);
                    }

                    //지도에 검색결과를 아이콘으로 오버레이
                    overlayMap();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void overlayMap() {
        //파싱된 결과를 맵에 표시하기
        for (PlaceVO place : mPlaceList) {
            Log.d("LDK", place.name);
            MarkerOptions opt = new MarkerOptions();
            opt.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
            opt.position(new LatLng(place.lat, place.lng));// 위도 • 경도
            opt.title(place.name); // 제목 미리보기
            opt.snippet(place.vicinity);

            mGoogleMap.addMarker(opt);

            //심화과제 1: 팻말을 클릭하면 place API중 detail 정보를 가져오는 API를호출하고,
            //info window에 홈페이지, 이미지, 전화번호, 등등을 표시
            //심화과제 2: 길찾기 서비스
        }
    }

    private void displayMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        //mGoogleMap = mapFragment.getMap();
        mGoogleMap = getMapFragment().getMap();

        //현재위치로 지도를 이동하고 zoom을 16레벨로 설정
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 16));

        mGoogleMap.clear();

        //현재 위치 아이콘 표시
        currentOpt = new MarkerOptions();
        currentOpt.icon(BitmapDescriptorFactory.fromResource(R.drawable.current));
        currentOpt.position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        currentOpt.title("현재위치"); // 제목 미리보기
        currentOpt.snippet(""); //위도 경도를 지오코딩으로 주소로 가져와서 뿌려준다.

        mGoogleMap.addMarker(currentOpt);
    }

    private MapFragment getMapFragment() {
        FragmentManager fm = null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            fm = getFragmentManager();
        } else {
            fm = getChildFragmentManager();
        }
        return (MapFragment) fm.findFragmentById(R.id.map);
    }

    GoogleApiClient.ConnectionCallbacks mConnectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
            Log.d("LDK", "connected");
            //가장 최근 위치 가져오기
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                Log.d("LDK", mLastLocation.getLatitude() + "," + mLastLocation.getLongitude());
                displayMap();
            }
            //실시간 위치추적
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, mLocationListener);
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.d("LDK", "connectionSuspended");
        }
    };

    GoogleApiClient.OnConnectionFailedListener mOnConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.d("LDK", "connect fail");
        }
    };

    //위치를 업데이트하는 리스너
    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLastLocation = location;
            displayMap();
        }
    };
}
