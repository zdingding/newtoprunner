package com.toprunner.ubii.toprunner.trace;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceOverlay;
import com.toprunner.ubii.toprunner.R;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TraceActivity extends Activity implements TraceListener,
		OnClickListener, OnCheckedChangeListener, OnItemSelectedListener {
	String TAG = "TraceActivity";
	private Button mGraspButton, mCleanFinishOverlay;

	private TextView mResultShow, mLowSpeedShow;
	private int mCoordinateType = LBSTraceClient.TYPE_AMAP;
	private MapView mMapView;
	private AMap mAMap;
	private LBSTraceClient mTraceClient;

	private String[] mRecordChooseArray;
	private List<TraceLocation> mTraceList;

	private static String mDistanceString, mStopTimeString;
	private static final String DISTANCE_UNIT_DES = " KM";
	private static final String TIME_UNIT_DES = " 分钟";
	private static final LatLng[] latlngs = new LatLng[] {
			new LatLng(39.9399010000,116.4887800000),
			new LatLng(39.9406680000,116.4887490000),
			new LatLng(39.9405750000,116.4890720000),
			new LatLng(39.9408380000,116.4893060000),
			new LatLng(39.9408380000,116.4893060000),
			new LatLng(39.9410760000,116.4892970000),
			new LatLng(39.9410690000,116.4898450000),
			new LatLng(39.9407620000,116.4898360000),
			new LatLng(39.9404710000,116.4892160000),
			new LatLng(39.9405960000,116.4890360000),
			new LatLng(39.9406610000,116.4887580000),
			new LatLng(39.9405820000,116.4885650000),
			new LatLng(39.9407510000,116.4882230000),
			new LatLng(39.9406890000,116.4881020000),
			new LatLng(39.9405960000,116.4880030000),
			new LatLng(39.9405470000,116.4878730000),
			new LatLng(39.9407270000,116.4877290000),
			new LatLng(39.9408480000,116.4878600000),
			new LatLng(39.9409520000,116.4878460000),
			new LatLng(39.9411900000,116.4880300000),
			new LatLng(39.9411700000,116.4881330000),
			new LatLng(39.9410830000,116.4881920000),
			new LatLng(39.9409550000,116.488183),
			new LatLng(39.9412490000,116.488192),
			new LatLng(39.9412530000,116.4884070000),
			new LatLng(39.9410760000,116.4884880000),
			new LatLng(39.9408900000,116.4884790000),
			new LatLng(39.9405850000,116.4885690000),
			new LatLng(39.9410760000,116.4884880000),
			new LatLng(39.9412530000,116.4884070000),
			new LatLng(39.9412490000,116.488192),
			new LatLng(39.9409550000,116.488183),
			new LatLng(39.9412460000,116.488205),//
			new LatLng(39.9418920000,116.48799),
			new LatLng(39.9422170000,116.487967),//
			new LatLng(39.94256,116.487967),//
			new LatLng(39.943092,116.48777),
			new LatLng(39.9430920000,116.48777),
			new LatLng(39.943538,116.48777),
			new LatLng(39.9436310000,116.487294),
			new LatLng(39.9436210000,116.4870150000),
			new LatLng(39.943994,116.486665),
			new LatLng(39.9438180000,116.486278),
			new LatLng(39.944527,116.485537),
			new LatLng(39.944644,116.485268),
			new LatLng(39.945218,116.484832),
			new LatLng(39.945996,116.484846),
			new LatLng(39.9463,116.485263),
			new LatLng(39.946418,116.485475),
			new LatLng(39.9466320000,116.485528),
			new LatLng(39.947016,116.485865),
			new LatLng(39.947026,116.486517),
			new LatLng(39.9471790000,116.486633),
			new LatLng(39.947265,116.486764),
			new LatLng(39.947282,116.487801),
			new LatLng(39.9475310000,116.487909),
			new LatLng(39.9475830000,116.488196),
			new LatLng(39.9477420000,116.488816),
			new LatLng(39.9478420000,116.489342),
			new LatLng(39.9476800000,116.489975),
			new LatLng(39.9473830000,116.490249),
			new LatLng(39.9471270000,116.490775),
			new LatLng(39.9468220000,116.491129),
			new LatLng(39.946193,116.491206),
			new LatLng(39.9460380000,116.491233),
			new LatLng(39.9449690000,116.49081),
			new LatLng(39.9447970000,116.490451),
			new LatLng(39.94442,116.490245),
			new LatLng(39.944475,116.489562),
			new LatLng(39.944288,116.488915),
			new LatLng(39.944046,116.488434),
			new LatLng(39.943908,116.48777),
			new LatLng(39.943521,116.48777),
			new LatLng(39.943908,116.48777),
			new LatLng(39.944022,116.487361),
			new LatLng(39.944219,116.487505),
			new LatLng(39.9444230000,116.487491),
			new LatLng(39.9444720000,116.487388),
			new LatLng(39.944637,116.487159),
			new LatLng(39.94489,116.487091),
			new LatLng(39.944852,116.486265),
			new LatLng(39.9458130000,116.485892),
			new LatLng(39.9462940000,116.485299),
			new LatLng(39.9458130000,116.485892),
			new LatLng(39.944852,116.486265),
			new LatLng(39.94489,116.487091),
			new LatLng(39.94488,116.487365),
			new LatLng(39.94527,116.487581),
			new LatLng(39.945346,116.487797),
			new LatLng(39.946736,116.487806),
			new LatLng(39.946722,116.487577),
			new LatLng(39.947268,116.487599),
			new LatLng(39.946722,116.487577),
			new LatLng(39.946736,116.487806),
			new LatLng(39.945346,116.487797),
			new LatLng(39.945277,116.488057),
			new LatLng(39.945073,116.488178),
			new LatLng(39.944897,116.488205),
			new LatLng(39.944561,116.488084),
			new LatLng(39.944468,116.487738),
			new LatLng(39.944589,116.487509),
			new LatLng(39.9448860000,116.487374),//
			new LatLng(39.9451390000,116.487451),//
			new LatLng(39.945274,116.487577),//
			new LatLng(39.94535,116.487806),//
			new LatLng(39.945256,116.488053),//
			new LatLng(39.9449070000,116.488214),//
			new LatLng(39.944918,116.488753),
			new LatLng(39.945395,116.488677),
			new LatLng(39.945554,116.488551),
			new LatLng(39.945723,116.488987),
			new LatLng(39.9461380000,116.488906),
			new LatLng(39.9461520000,116.489697),
			new LatLng(39.94648,116.489926),
			new LatLng(39.946529,116.489908),
			new LatLng(39.946729,116.490137),
			new LatLng(39.946916,116.490177),
			new LatLng(39.946968,116.49011),
			new LatLng(39.94702,116.490366),
			new LatLng(39.947137,116.490393),
			new LatLng(39.947223,116.490137),
			new LatLng(39.947334,116.490056),
			new LatLng(39.947386,116.490236)

	};
	private ConcurrentMap<Integer, TraceOverlay> mOverlayList = new ConcurrentHashMap<Integer, TraceOverlay>();
	private int mSequenceLineID = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trace);
		mGraspButton = (Button) findViewById(R.id.grasp_button);
		mCleanFinishOverlay = (Button) findViewById(R.id.clean_finish_overlay_button);
		mCleanFinishOverlay.setOnClickListener(this);
		mGraspButton.setOnClickListener(this);
		mMapView = (MapView) findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);// 此方法必须重写
		mResultShow = (TextView) findViewById(R.id.show_all_dis);
		mLowSpeedShow = (TextView) findViewById(R.id.show_low_speed);
		mDistanceString = getResources().getString(R.string.distance);
		mStopTimeString = getResources().getString(R.string.stop_time);
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		if (mAMap == null) {
			mAMap = mMapView.getMap();
			mAMap.getUiSettings().setRotateGesturesEnabled(false);
			mAMap.getUiSettings().setZoomControlsEnabled(false);
		}
		mTraceList = TraceAsset.parseLocationsData(this.getAssets(),
				"traceRecord" + File.separator + "AMapTrace.txt");
		mRecordChooseArray = TraceAsset.recordNames(this.getAssets());
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mRecordChooseArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mMapView != null) {
			mMapView.onDestroy();
		}
	}

	/**
	 * 轨迹纠偏失败回调
	 */
	@Override
	public void onRequestFailed(int lineID, String errorInfo) {
		Toast.makeText(this.getApplicationContext(), errorInfo,
				Toast.LENGTH_SHORT).show();
		if (mOverlayList.containsKey(lineID)) {
			TraceOverlay overlay = mOverlayList.get(lineID);
			overlay.setTraceStatus(TraceOverlay.TRACE_STATUS_FAILURE);
			setDistanceWaitInfo(overlay);
		}
	}

	/**
	 * 轨迹纠偏过程回调
	 */
	@Override
	public void onTraceProcessing(int lineID, int index, List<LatLng> segments) {
		Log.d(TAG, "onTraceProcessing");
		if (segments == null) {
			return;
		}
		if (mOverlayList.containsKey(lineID)) {
			TraceOverlay overlay = mOverlayList.get(lineID);
			overlay.setTraceStatus(TraceOverlay.TRACE_STATUS_PROCESSING);
			overlay.add(Arrays.asList(latlngs));
		}
	}

	/**
	 * 轨迹纠偏结束回调
	 */
	@Override
	public void onFinished(int lineID, List<LatLng> linepoints, int distance,
						   int watingtime) {
		Log.d(TAG, "onFinished");
		Toast.makeText(this.getApplicationContext(), "onFinished",
				Toast.LENGTH_SHORT).show();
		if (mOverlayList.containsKey(lineID)) {
			TraceOverlay overlay = mOverlayList.get(lineID);
			overlay.setTraceStatus(TraceOverlay.TRACE_STATUS_FINISH);
			overlay.setDistance(distance);
			overlay.setWaitTime(watingtime);
			setDistanceWaitInfo(overlay);
		}

	}

	/**
	 * 调起一次轨迹纠偏
	 */
	private void traceGrasp() {
		if (mOverlayList.containsKey(mSequenceLineID)) {
			TraceOverlay overlay = mOverlayList.get(mSequenceLineID);
			//overlay.zoopToSpan();
			int status = overlay.getTraceStatus();
			String tipString = "";
			if (status == TraceOverlay.TRACE_STATUS_PROCESSING) {
				tipString = "该线路轨迹纠偏进行中...";
				setDistanceWaitInfo(overlay);
			} else if (status == TraceOverlay.TRACE_STATUS_FINISH) {
				setDistanceWaitInfo(overlay);
				tipString = "该线路轨迹已完成";
			} else if (status == TraceOverlay.TRACE_STATUS_FAILURE) {
				tipString = "该线路轨迹失败";
			} else if (status == TraceOverlay.TRACE_STATUS_PREPARE) {
				tipString = "该线路轨迹纠偏已经开始";
			}
			Toast.makeText(this.getApplicationContext(), tipString,
					Toast.LENGTH_SHORT).show();
			return;
		}
		TraceOverlay mTraceOverlay = new TraceOverlay(mAMap);
		mOverlayList.put(mSequenceLineID, mTraceOverlay);
		List<LatLng> mapList = traceLocationToMap(mTraceList);
		//点A

		mTraceOverlay.setProperCamera(mapList);
		mResultShow.setText(mDistanceString);
		mLowSpeedShow.setText(mStopTimeString);
		mTraceClient = new LBSTraceClient(this.getApplicationContext());
		mTraceClient.queryProcessedTrace(mSequenceLineID, mTraceList,
				mCoordinateType, this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.grasp_button:
			traceGrasp();
			break;
		case R.id.clean_finish_overlay_button:
			cleanFinishTrace();
			break;
		}
	}

	/**
	 * 清除地图已完成或出错的轨迹
	 */
	private void cleanFinishTrace() {
		Iterator iter = mOverlayList.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Integer key = (Integer) entry.getKey();
			TraceOverlay overlay = (TraceOverlay) entry.getValue();
			if (overlay.getTraceStatus() == TraceOverlay.TRACE_STATUS_FINISH
					|| overlay.getTraceStatus() == TraceOverlay.TRACE_STATUS_FAILURE) {
				overlay.remove();
				mOverlayList.remove(key);
			}
		}
	}

	/**
	 * 设置显示总里程和等待时间
	 *
	 * @param overlay
	 */
	private void setDistanceWaitInfo(TraceOverlay overlay) {
		int distance = -1;
		int waittime = -1;
		if (overlay != null) {
			distance = overlay.getDistance();
			waittime = overlay.getWaitTime();
		}
		DecimalFormat decimalFormat = new DecimalFormat("0.0");
		;
		mResultShow.setText(mDistanceString
				+ decimalFormat.format(distance / 1000d) + DISTANCE_UNIT_DES);
		mLowSpeedShow.setText(mStopTimeString
				+ decimalFormat.format(waittime / 60d) + TIME_UNIT_DES);
	}

	/**
	 * 坐标系类别选择回调
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		default:
			mCoordinateType = LBSTraceClient.TYPE_AMAP;
		}
	}

	/**
	 * Spinner 下拉选择
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		if (mTraceList != null) {
			mTraceList.clear();
		}
		mSequenceLineID = 1000 + pos;
		mTraceList = TraceAsset.parseLocationsData(
				TraceActivity.this.getAssets(), "traceRecord" + File.separator
						+ mRecordChooseArray[pos]);
		String recordName = mRecordChooseArray[pos];
		if (recordName == null) {
			return;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback
	}

	/**
	 * 轨迹纠偏点转换为地图LatLng
	 *
	 * @param traceLocationList
	 * @return
	 */
	public List<LatLng> traceLocationToMap(List<TraceLocation> traceLocationList) {
		List<LatLng> mapList = new ArrayList<LatLng>();
		for (TraceLocation location : traceLocationList) {
			LatLng latlng = new LatLng(location.getLatitude(),
					location.getLongitude());
			mapList.add(latlng);
		}
		return mapList;
	}
}
