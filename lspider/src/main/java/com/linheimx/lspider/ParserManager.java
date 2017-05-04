package com.linheimx.lspider;

import com.linheimx.lspider.zimuku.parser.MoviesParser;
import com.linheimx.lspider.zimuku.parser.ZimuDownloadPageParser;

/**
 * Created by x1c on 2017/5/1.
 */

public class ParserManager {

    private static ParserManager _ParserManager;

    private ParserManager() {

    }

    public static synchronized ParserManager getInstance() {
        if (_ParserManager == null) {
            _ParserManager = new ParserManager();
        }
        return _ParserManager;
    }

    MoviesParser _MoviesParser;

    public MoviesParser get_MoviesParser() {
        if (_MoviesParser == null) {
            _MoviesParser = new MoviesParser();
        }
        return _MoviesParser;
    }

    ZimuDownloadPageParser _ZimuDownloadPageParser;

    public ZimuDownloadPageParser get__ZimuDownloadPageParser() {
        if (_ZimuDownloadPageParser == null) {
            _ZimuDownloadPageParser = new ZimuDownloadPageParser();
        }
        return _ZimuDownloadPageParser;
    }
}
