package com.lw.project.search.service;

import com.lw.common.to.SkuEsModel;

import java.io.IOException;
import java.util.List;

public interface ProductSaveService {

    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
