package com.cguim.cgufollowme;

import min3d.core.Object3dContainer;
import min3d.core.RendererActivity;
import min3d.parser.IParser;
import min3d.parser.Parser;
import min3d.vos.Light;

public class MapActivity extends RendererActivity {

	private Object3dContainer model;

	private float theta;
	private float pos_z = 0, pos_x = 0;
	private float old_pos_z = 0, old_pos_x = 0;
	private float move_X = 0, move_Y = 0;
	private float dX_M = 0, dY_M = 0;

	private float degrees_X = 0, degrees_Y = 0;
	private float radians_X, radians_Y;
	private float degrees_X_R, degrees_Y_R;
	private float radians_X_R, radians_Y_R;
	private float dX_R = 0, dY_R = 0;
	private float gTouchX1, gTouchY1, gTouchX2, gTouchY2;
	private float oldDistance, newDistance;
	private float CAM_RADIUS = 30;
	private float tempRadius = 30;
	private MyApplication myApp;

	private SensorManager sensorManager;
	private Sensor aSensor;
	private Sensor mSensor;

	float[] accelerometerValues = new float[3];
	float[] magneticFieldValues = new float[3];
	float[] smooth_accelerometerValues = new float[3];
	float[] smooth_magneticFieldValues = new float[3];

	TextView title;
	ToggleButton autoRotate, moveModel;
	Spinner spn;
	Button btn_over;
	ArrayAdapter<String> adapter;
	String[] PATH;
	String destination = null, location = null;
	int path = 0, step = 0;

	boolean rotate = false, had_last_position = false, move = false;

	@Override
	@Override
	protected void onCreateSetContentView() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map_layout);
		// 說明手勢
		LayoutInflater inflater = LayoutInflater.from(MapActivity.this);
		View layout = inflater.inflate(R.layout.gesture, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
		builder.setView(layout);
		builder.setTitle("使用說明");
		builder.setMessage("透過觸控手勢\n調整模型視角及縮放大小\n平行移動模型位置及指南針定位");
		builder.setNegativeButton("確定", null);

		builder.create().show();

		// *********判別路徑**************
		myApp = (MyApplication) getApplication();
		location = myApp.getLocation(); // 起點(現在位置)
		destination = myApp.getDestination(); // 終點(目的地)
		
		pathway(location, destination);

		LinearLayout ll = (LinearLayout) this.findViewById(R.id.scene1Holder);
		ll.addView(_glSurfaceView);

		title = (TextView) this.findViewById(R.id.mapTitle);
		title.setText("目的地:" + destination);

		autoRotate = (ToggleButton) this.findViewById(R.id.toggleButton1);
		autoRotate.setOnCheckedChangeListener(onChangeRotate);

		moveModel = (ToggleButton) this.findViewById(R.id.toggleButton2);
		moveModel.setOnCheckedChangeListener(onChangeMove);

		btn_over = (Button) this.findViewById(R.id.btn_over);
		btn_over.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sensorManager.unregisterListener(myListener);
				finish();
			}
		});

		spn = (Spinner) this.findViewById(R.id.spn_sub_place);
		adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, PATH);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spn.setAdapter(adapter);
		spn.setSelection(0, false);
		spn.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				step = position;
				onPause();
				onResume();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		initSensor();
	}

	@Override
	@Override
	@Override
	public void initScene() {

		Light light1 = new Light();
		Light light2 = new Light();
		Light light3 = new Light();
		light1.position.setAll(3, 3, 0);
		light2.position.setAll(0, 3, 3);
		light3.position.setAll(0, 6, 0);
		scene.lights().add(light1);
		scene.lights().add(light2);
		scene.lights().add(light3);

		IParser parser = Parser.createParser(Parser.Type.MAX_3DS,
				getResources(), "com.cguim.cgufollowme:raw/path" + path + "_"
						+ step, false);
		parser.parse();

		model = parser.getParsedObject();
		model.scale().x = model.scale().y = model.scale().z = 2f;
		// model.position().y = -1;
		// model.position().x = 7.5f;
		// model.position().z = -2;
		scene.addChild(model);

		// scene.camera().target = model.position();

		scene.camera().position.x = 30;
		scene.camera().position.y = 0;
		scene.camera().position.z = 0;
	}

	@Override
	@Override
	@Override
	public void updateScene() {
		// Log.i("updateScene", "updateScene()");
		/** 指針模式---指針模式---指針模式---指針模式---指針模式---指針模式---指針模式---指針模式---指針模式--- */
		if (rotate) {
			radians_X_R = degrees_X_R * ((float) Math.PI / 180);
			radians_Y_R = degrees_Y_R * ((float) Math.PI / 180);

			scene.camera().position.y = (float) Math.sin(radians_Y_R)
					* CAM_RADIUS;
			scene.camera().position.x = (float) Math.cos(radians_X_R)
					* (float) Math.cos(radians_Y_R) * CAM_RADIUS;
			scene.camera().position.z = (float) Math.sin(radians_X_R)
					* (float) Math.cos(radians_Y_R) * CAM_RADIUS;
		} else {
			/** 旋轉模式---旋轉模式---旋轉模式---旋轉模式---旋轉模式---旋轉模式---旋轉模式---旋轉模式--- */
			// 點過指針，紀錄是否有最後位置
			if (had_last_position) {
				dX_R = degrees_X_R * 6;
				dY_R = degrees_Y_R * 6;
				had_last_position = false;
			}

			dX_R = dX_R % 2160;// 360度*6pixel，限制dX 0~2160一個循環

			degrees_X = dX_R / 6; // 移動6個pixel算1度
			degrees_Y = dY_R / 6;

			if (degrees_Y < 0) {
				dY_R = 0;
				degrees_Y = 0;
			} else if (degrees_Y > 89) {
				dY_R = 534;
				degrees_Y = 89;
			}
			radians_X = degrees_X * ((float) Math.PI / 180);
			radians_Y = degrees_Y * ((float) Math.PI / 180);

			scene.camera().position.y = (float) Math.sin(radians_Y)
					* CAM_RADIUS;
			scene.camera().position.x = (float) Math.cos(radians_X)
					* (float) Math.cos(radians_Y) * CAM_RADIUS;
			scene.camera().position.z = (float) Math.sin(radians_X)
					* (float) Math.cos(radians_Y) * CAM_RADIUS;
		}
		/** 平移模式---平移模式---平移模式---平移模式---平移模式---平移模式---平移模式---平移模式--- */
		if (move) {
			if (rotate)
				theta = degrees_X_R * ((float) Math.PI / 180);
			else
				theta = degrees_X * ((float) Math.PI / 180);

			move_X = dX_M / 50;
			move_Y = dY_M / 50;
			if (move_X == 0 && move_Y == 0) {
				pos_z = old_pos_z;
				pos_x = old_pos_x;
			} else {
				pos_z = ((float) Math.sin(theta) * move_Y - (float) Math
						.cos(theta) * move_X)
						+ old_pos_z;
				pos_x = ((float) Math.sin(theta) * move_X + (float) Math
						.cos(theta) * move_Y)
						+ old_pos_x;
			}

			model.position().z = pos_z;
			model.position().x = pos_x;
		}
	}

	private GestureDetector detector = new GestureDetector(getBaseContext(),
			new OnGestureListener() {

				@Override
				public boolean onSingleTapUp(MotionEvent e) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public void onShowPress(MotionEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public boolean onScroll(MotionEvent e1, MotionEvent e2,
						float distanceX, float distanceY) {
					// TODO Auto-generated method stub
					if (move) {
						dX_M = dX_M - distanceX;
						dY_M = dY_M - distanceY;
					} else {
						dX_R = dX_R - distanceX;
						dY_R = dY_R - distanceY;
						// hasChange = true;
					}
					return true;
				}

				@Override
				public void onLongPress(MotionEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2,
						float velocityX, float velocityY) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean onDown(MotionEvent e) {
					// TODO Auto-generated method stub
					return false;
				}
			});

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int pointerCount = event.getPointerCount();
		int action = MotionEventCompat.getActionMasked(event);

		if (pointerCount == 1) {
			if (detector.onTouchEvent(event)) {
				return true;
			} else {
				return false;
			}
		} else if (pointerCount == 2) {
			gTouchX1 = (float) event.getX(0); // 第一個觸控點
			gTouchY1 = (float) event.getY(0);
			gTouchX2 = (float) event.getX(1); // 第二個觸控點
			gTouchY2 = (float) event.getY(1);
			switch (action) {
			case MotionEvent.ACTION_POINTER_DOWN:// 第二個觸控點被點下
				oldDistance = (float) Math.sqrt(Math.pow((gTouchX1 - gTouchX2),
						2) + Math.pow((gTouchY1 - gTouchY2), 2));
				return true;
			case MotionEvent.ACTION_MOVE:
				newDistance = (float) Math.sqrt(Math.pow((gTouchX1 - gTouchX2),
						2) + Math.pow((gTouchY1 - gTouchY2), 2));
				CAM_RADIUS = tempRadius / newDistance * oldDistance;
				if (CAM_RADIUS < 5)
					CAM_RADIUS = 5;
				else if (CAM_RADIUS > 80)
					CAM_RADIUS = 80;
				return true;
			case MotionEvent.ACTION_POINTER_UP:
				tempRadius = CAM_RADIUS;
				return true;
			}

		}
		return false;
	}

	private OnCheckedChangeListener onChangeRotate = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) { // 註冊感應器
				rotate = true;
				had_last_position = true;
				sensorManager.registerListener(myListener, aSensor,
						SensorManager.SENSOR_DELAY_GAME);
				sensorManager.registerListener(myListener, mSensor,
						SensorManager.SENSOR_DELAY_GAME);
			} else { // 取消註冊感應器
				rotate = false;
				sensorManager.unregisterListener(myListener);
			}
		}
	};

	private OnCheckedChangeListener onChangeMove = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) { // 開啟平移
				dX_M = 0;
				dY_M = 0;
				move = true;
			} else { // 關閉平移
				move = false;
				old_pos_z = pos_z;
				old_pos_x = pos_x;
			}
		}
	};

	private void initSensor() {
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		aSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		calculateOrientation();
	}

	private void calculateOrientation() {
		// 計算旋轉角度

		float[] values = new float[3];
		float[] R = new float[9];
		SensorManager.getRotationMatrix(R, null, smooth_accelerometerValues,
				smooth_magneticFieldValues);// 取得旋轉矩陣(使用降噪值)
		SensorManager.getOrientation(R, values);

		// 要經過一次數據格式的轉換，轉換為度
		degrees_X_R = (float) Math.toDegrees(values[0]);// 水平旋轉
		degrees_Y_R = (float) Math.toDegrees(values[1]);// 前後俯仰
		// values[2] = (float) Math.toDegrees(values[2]);//左右翻滾

		if (degrees_X_R > 0)
			degrees_X_R = (degrees_X_R);
		else
			degrees_X_R = (degrees_X_R) % 360;

		if (degrees_Y_R > 0)
			degrees_Y_R = 90 - degrees_Y_R;
		else
			degrees_Y_R = 90 + degrees_Y_R;

	}

	final SensorEventListener myListener = new SensorEventListener() {
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			Log.i("Sensor_test", "onAccuracyChanged: " + sensor
					+ ", accuracy: " + accuracy);
		}

		public void onSensorChanged(SensorEvent sensorEvent) {
			// TODO Auto-generated method stub
			if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				accelerometerValues = sensorEvent.values;
				smooth_accelerometerValues = exponentialSmoothing(
						accelerometerValues, smooth_accelerometerValues,
						0.0618f);
			}

			if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				magneticFieldValues = sensorEvent.values;
				smooth_magneticFieldValues = exponentialSmoothing(
						magneticFieldValues, smooth_magneticFieldValues,
						0.0618f);
			}
			calculateOrientation();
		}
	};

	private float[] exponentialSmoothing(float[] input, float[] output,
			float alpha) {
		// 指數平滑法
		if (output == null)
			return input;
		for (int i = 0; i < input.length; i++) {
			output[i] = output[i] + alpha * (input[i] - output[i]);
		}
		return output;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		sensorManager.unregisterListener(myListener);
		finish();
	}

	public void pathway(String loc, String des) {
		if (loc.equals("")) {
			PATH = getResources().getStringArray(R.array.nopath);
			if (destination.equals("資訊管理學系系辦")) {
				path = 0;
				step = 2;
			} else {
				path = 2;
				step = 0;
			}
		} else {
			if (loc.equals("長庚大學-候車亭") && des.equals("資訊管理學系系辦")) {
				PATH = getResources().getStringArray(R.array.path0);
				path = 0;
			} else if (loc.equals("長庚大學-第一醫學大樓1F") && des.equals("資訊管理學系系辦")) {
				PATH = getResources().getStringArray(R.array.path1);
				path = 1;
			}else{
				PATH = getResources().getStringArray(R.array.path1);
				path = 2;
				step = 0;
			}
		}

	}

}
