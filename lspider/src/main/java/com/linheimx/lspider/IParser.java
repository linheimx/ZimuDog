package com.linheimx.lspider;

/**
 * Created by x1c on 2017/5/1.
 */

public interface IParser<I, O> {
    O parse(I i);
}
