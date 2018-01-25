package xxzx.baseMapState.MainMap;

import xxzx.baseMapState.MainMap.BaseMainMapState;

public class MapStateContext {

	private BaseMainMapState mainState;
	// 持有一个BaseMapState类型的对象实例
	private BaseMainMapState currentState;


	/**
	 * 用于activity返回的接口方法
	 */
	public void BackKeyDown() {
		// 转调state来处理
		if(currentState!=null) {
			currentState.BackKeyDown();
		}
	}
//	/**
//	 * 用于activity菜单键的接口方法
//	 */
//	public void MenuKeyDown() {
//		// 转调state来处理
//		currentState.MenuKeyDown();
//	}

	public BaseMainMapState getCurrentState(){
		return currentState;
	}
	public void setState(BaseMainMapState state) {
		//首先关闭
		if(this.currentState!=null){
			this.currentState.Close();
		}
		this.currentState = state;
		this.currentState.setContext(this);
	}

	public BaseMainMapState getMainState() {
		return mainState;
	}

	public void setMainState(BaseMainMapState mainState) {
		this.mainState = mainState;
	}

	//初始化当前状态下的控件
	public void InitViewAndData(){
		currentState.InitViewAndData();
	}

	public void backMainState(){
		if(this.currentState!=null){
			this.currentState.Close();
		}
		this.currentState=this.mainState;
	}

	public void close(){
		if(this.currentState!=null){
			this.currentState.Close();
		}
		if(this.mainState!=null){
			this.mainState.Close();
		}
	}
}
