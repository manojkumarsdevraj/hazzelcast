package com.unilog.products;


public class CartAndGroupController {
	private static CartAndGroupController cartAndGroupController=null;
	private boolean reInitializeClient = true;
	
	private CartAndGroupController() {
		if(reInitializeClient){
			initializeClient();
		}
	}
	private void initializeClient() {
		reInitializeClient = false;
		

	}
	public static CartAndGroupController getInstance() {
		synchronized(CartAndGroupController.class) {
			if(cartAndGroupController==null) {
				cartAndGroupController = new CartAndGroupController();				
			}
		}
		return cartAndGroupController;
	}
	
	
	public boolean isReInitializeClient() {
		return reInitializeClient;
	}
	public void setReInitializeClient(boolean reInitializeClient) {
		this.reInitializeClient = reInitializeClient;
	}
	
}
