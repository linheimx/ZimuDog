package com.linheimx.zimudog.vp.search;

import android.support.annotation.NonNull;

import com.linheimx.lspider.god.GodItem;
import com.linheimx.lspider.god.IMovie;
import com.linheimx.zimudog.vp.base.IPresenter;

import java.util.List;

/**
 * Created by x1c on 2017/5/1.
 */

public interface IContract {

    interface P extends IPresenter {
        void searchByKW(@NonNull String keyWord);

        void loadMore();

        void cancelSearch();

        void release();
    }

    interface V {
        void showItems(List<GodItem> movies, boolean hasMore);

        void showLoadingError();
    }
}
