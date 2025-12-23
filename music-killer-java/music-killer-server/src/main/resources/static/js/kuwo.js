function generateReqId(e, t, r) {
    var n,
        o,
        d = 0,
        h = 0;
    var i = (t && r) || 0,
        b = t || [],
        f = undefined,
        v = undefined;
    var m = {
        0: 43,
        1: 64,
        2: 160,
        3: 14,
        4: 221,
        5: 55,
        6: 249,
        7: 97,
        8: 86,
        9: 170,
        10: 120,
        11: 218,
        12: 66,
        13: 188,
        14: 238,
        15: 102,
    };
    (f = [1 | m[0], m[1], m[2], m[3], m[4], m[5]]),
    (v = 16383 & ((m[6] << 8) | m[7]));
    let y = new Date().getTime(),
        w = h + 1,
        dt = y - d + (w - h) / 1e4;
    if ((dt < 0 && (v = (v + 1) & 16383), (dt < 0 || y > d) && (w = 0), w >= 1e4))
        throw new Error("uuid.v1(): Can't create more than 10M uuids/sec");
    (d = y), (h = w), (o = v);
    var A = (1e4 * (268435455 & (y += 122192928e5)) + w) % 4294967296;
    (b[i++] = (A >>> 24) & 255),
        (b[i++] = (A >>> 16) & 255),
        (b[i++] = (A >>> 8) & 255),
        (b[i++] = 255 & A);
    var x = ((y / 4294967296) * 1e4) & 268435455;
    (b[i++] = (x >>> 8) & 255),
        (b[i++] = 255 & x),
        (b[i++] = ((x >>> 24) & 15) | 16),
        (b[i++] = (x >>> 16) & 255),
        (b[i++] = (v >>> 8) | 128),
        (b[i++] = 255 & v);
    for (var T = 0; T < 6; ++T) b[i + T] = f[T];

    return c(b);
}

function c(e, t) {
    for (var r = [], i = 0; i < 256; ++i) r[i] = (i + 256).toString(16).substr(1);
    var i = t || 0,
        n = r;
    return [
        n[e[i++]],
        n[e[i++]],
        n[e[i++]],
        n[e[i++]],
        "-",
        n[e[i++]],
        n[e[i++]],
        "-",
        n[e[i++]],
        n[e[i++]],
        "-",
        n[e[i++]],
        n[e[i++]],
        "-",
        n[e[i++]],
        n[e[i++]],
        n[e[i++]],
        n[e[i++]],
        n[e[i++]],
        n[e[i++]],
    ].join("");
}

/**
 * 根据 cookie 中指定 key、value 生成请求所需要的私钥
 *
 * @param t kywo.value cookie中 key 对应的 value
 * @param e kuwo.key 写死的值
 * @returns {string|string|null} header：Secret
 */
function generateSecret(t, e) {
    if (null == e || e.length <= 0)
        return null;
    for (var n = "", i = 0; i < e.length; i++)
        n += e.charCodeAt(i).toString();
    var o = Math.floor(n.length / 5)
        , r = parseInt(n.charAt(o) + n.charAt(2 * o) + n.charAt(3 * o) + n.charAt(4 * o) + n.charAt(5 * o))
        , c = Math.ceil(e.length / 2)
        , l = Math.pow(2, 31) - 1;
    if (r < 2)
        return null;
    var d = Math.round(1e9 * Math.random()) % 1e8;
    for (n += d; n.length > 10;)
        n = (parseInt(n.substring(0, 10)) + parseInt(n.substring(10, n.length))).toString();
    n = (r * n + c) % l;
    var f = ""
        , h = "";
    for (i = 0; i < t.length; i++)
        h += (f = parseInt(t.charCodeAt(i) ^ Math.floor(n / l * 255))) < 16 ? "0" + f.toString(16) : f.toString(16),
            n = (r * n + c) % l;
    for (d = d.toString(16); d.length < 8;)
        d = "0" + d;
    return h += d
}