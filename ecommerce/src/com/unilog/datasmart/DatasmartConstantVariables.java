package com.unilog.datasmart;

public interface DatasmartConstantVariables {
	//Content Object Variables
	final String BRAND_ARI_CODE="brandARICode";
	final String BRAND_NAME="brandName";
	final String IS_ARI_PAGE="isARIPage";
	final String BRAND_PREFIX_CODE="brandPrefixCode";
	final String BRAND_ID="brandId";
	final String IMAGE="image";
	final String REQ_TYPE="reqType";
	final String IS_ARI_BRAND="isARIBrand";
	final String ARI_PAGE_HEADING="ariPageHeading";
	final String URL_PARAMS="urlParams";
	final String URL_PARAM="urlParam";
	final String SUBSET="subset";
	final String GENERAL_SUBSET="generalSubset";
	
	//Request Object Variables
	final String PARENT_ID="parentId";
	final String ASSEMBLY_ID="assemblyId";
	final String MODEL_ID="modelId";
	final String WIDTH="width";
	final String ZOOM_LEVEL="zoomLevel";
	final String MODEL_NAME="modelName";
	final String NO_OF_RESULTS="numberOfResults";
	final String PART_SKU="partSku";
	final String PAGE_SIZE="pageSize";
	final String PAGE="page";
	final String SKU="sku";
	final String SEARCH="search";
	final String MODEL_SEARCH="modelSearch";
	final String PART_SEARCH="partSearch";
	final String BRAND_CODE="brandCode";
	final String PART_ID="partId";
	
	
	
	
	
	//ARI Call Enums
	public enum ARIServices {
		nodeChildren, 
		assemblyImage,
		searchModel,
		searchModelAssemblies,
		searchModelAssembliesWithInfo,
		searchParts,
		searchPartModels,
		searchPartsWithinModel,
		assemblyInfo,
		assemblyInfoNoHotSpot,
		modelAutoComplete,
		partAutoComplete,
		hotSpots,
		partInfo,
		;
	}
}
