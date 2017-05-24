package com.linheimx.lspider;

import com.linheimx.lspider.shooter.parser.PageParserShooter;
import com.linheimx.lspider.zimuku.parser.AllZimuParser;
import com.linheimx.lspider.zimuku.parser.PageParser_Zimuku;
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

    PageParser_Zimuku _PageParserZimuku;

    public PageParser_Zimuku get_PageParserZimuku() {
        if (_PageParserZimuku == null) {
            _PageParserZimuku = new PageParser_Zimuku();
        }
        return _PageParserZimuku;
    }

    PageParserShooter _PageParserShooter;

    public PageParserShooter get_PageParserShooter() {
        if (_PageParserShooter == null) {
            _PageParserShooter = new PageParserShooter();
        }
        return _PageParserShooter;
    }


    ZimuDownloadPageParser _ZimuDownloadPageParser;

    public ZimuDownloadPageParser get__ZimuDownloadPageParser() {
        if (_ZimuDownloadPageParser == null) {
            _ZimuDownloadPageParser = new ZimuDownloadPageParser();
        }
        return _ZimuDownloadPageParser;
    }

    AllZimuParser _AllZimuParser;

    public AllZimuParser get__AllZimuParser() {
        if (_AllZimuParser == null) {
            _AllZimuParser = new AllZimuParser();
        }
        return _AllZimuParser;
    }
}
