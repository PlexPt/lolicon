package com.github.plexpt.lolicon;


import com.alibaba.fastjson.JSON;
import com.github.plexpt.lolicon.lolicon.aop.TimeLog;

import net.dreamlu.mica.http.HttpRequest;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoliconBotApi {


    /**
     * @param args
     */
    public static void main(String[] args) {

        System.out.println(JSON.toJSONString(get(),true));
    }

    /**
     * API v2
     * GET https://api.lolicon.app/setu/v2
     * 或
     * <p>
     * POST https://api.lolicon.app/setu/v2
     * Content-Type: application/json
     * <p>
     * <p>
     * 请求
     * 参数名	数据类型	默认值	说明
     * r18	int	0	0为非 R18，1为 R18，2为混合（在库中的分类，不等同于作品本身的 R18 标识）
     * num	int	1	一次返回的结果数量，范围为1到100；在指定关键字或标签的情况下，结果数量可能会不足指定的数量
     * uid	int[]		返回指定uid作者的作品，最多20个
     * keyword	string		返回从标题、作者、标签中按指定关键字模糊匹配的结果，大小写不敏感，性能和准度较差且功能单一，建议使用tag代替
     * tag	string[]		返回匹配指定标签的作品，详见下文
     * size	string[]	["original"]	返回指定图片规格的地址，详见下文
     * proxy	string	i.pixiv.cat	设置图片地址所使用的在线反代服务，详见下文
     * dateAfter	int		返回在这个时间及以后上传的作品；时间戳，单位为毫秒
     * dateBefore	int		返回在这个时间及以前上传的作品；时间戳，单位为毫秒
     * dsc	boolean	false	设置为任意真值以禁用对某些缩写keyword和tag的自动转换，详见下文
     * 关于数组形式的参数
     * GET 请求可以通过追加同名参数来发送数组，例如
     * <p>
     * GET https://api.lolicon.app/setu/v2?size=original&size=regular
     * 在 POST 请求中，如果数组内仅有一个项，可以将数组省略，例如{ "size": ["regular"] }和{ "size": "regular" }是等价的
     * <p>
     * tag
     * 可以自由按照 AND 和 OR 规则来匹配标签、作者名、标题，所有匹配均大小写不敏感
     * <p>
     * 其中标签和作者名使用 n-gram (2≤n≤3) 分词匹配，标题为完全匹配
     * <p>
     * 参数数组内的每个字符串之间应用 AND 规则（最多3个）；每个字符串可以是若干个由|分隔的标签，它们之间应用 OR 规则（最多20个）
     * <p>
     * 举个例子，我需要查找“(萝莉或少女)的(白丝或黑丝)的色图”，即 (萝莉 OR 少女) AND (白丝 OR 黑丝)，那么可以这样发送请求
     * <p>
     * GET https://api.lolicon.app/setu/v2?tag=萝莉|少女&tag=白丝|黑丝
     * POST https://api.lolicon.app/setu/v2
     * Content-Type: application/json
     * <p>
     * {
     * "tag": [
     * "萝莉|少女",
     * "白丝|黑丝"
     * ]
     * }
     * 特别地，对于 POST 请求，你可以直接发送二维数组，不需要特地使用|拼接
     * <p>
     * POST https://api.lolicon.app/setu/v2
     * Content-Type: application/json
     * <p>
     * {
     * "tag": [
     * ["萝莉", "少女"],
     * ["白丝", "黑丝"],
     * ]
     * }
     * size
     * 以下为五种规格的示例（SFW，请放心打开）
     * <p>
     * 规格	地址
     * original	https://i.pixiv.cat/img-original/img/2021/06/14/17/25/59/90551655_p0.jpg
     * regular	https://i.pixiv.cat/img-master/img/2021/06/14/17/25/59/90551655_p0_master1200.jpg
     * small	https://i.pixiv.cat/c/540x540_70/img-master/img/2021/06/14/17/25/59
     * /90551655_p0_master1200.jpg
     * thumb	https://i.pixiv.cat/c/250x250_80_a2/img-master/img/2021/06/14/17/25/59
     * /90551655_p0_square1200.jpg
     * mini	https://i.pixiv.cat/c/48x48/img-master/img/2021/06/14/17/25/59/90551655_p0_square1200
     * .jpg
     * 你可能发现了，small,thumb,mini这些规格的地址中的参数其实是可调的而非定死的，因此如果你有需求，可以造出一个特定大小的缩略图，详见下面的proxy说明
     * <p>
     * 若size参数不符合上面任何一个规格，最终返回的urls将为一个空对象{}
     * <p>
     * proxy
     * 由于P站资源域名i.pximg.net具有防盗链措施，不含www.pixiv.net referer 的请求均会 403，所以如果需要直接在网页上展示或在客户端上直接下载必须依靠反代服务
     * <p>
     * 你可以设置为任何假值("",0,false,null)来得到原始的 i.pximg.net 图片地址
     * 当不指定协议时，会自动补充https://
     * 可使用以下占位符
     * 占位符	说明	实际值（以 90551655_p0 为例）
     * {{pid}}	作品 pid	90551655
     * {{p}}	作品所在页	0
     * {{uid}}	作者 uid	43454954
     * {{ext}}	图片扩展名 (original)	jpg
     * {{path}}	图片地址的相对路径	根据规格不同而不同
     * {{datePath}}	相对路径中的日期部分	2021/06/14/17/25/59
     * 例：以下四种是等价的
     * <p>
     * i.pixiv.cat
     * https://i.pixiv.cat
     * i.pixiv.cat/{{path}}
     * https://i.pixiv.cat/{{path}}
     * 若你使用了占位符，但没有用到{{path}}，则size参数是无意义的，不管什么规格返回的地址都将相同
     * <p>
     * 利用占位符自定义缩略图规格
     * 上面说到可以造特定大小的缩略图，格式大概是这样
     * <p>
     * https://i.pixiv.cat/c/<size>x<size>/img-master/img/{{datePath}}/{{pid}}_p{{p}}_<master
     * |square>1200.jpg
     * <size>x<size>这里长宽必须相同，最大为600x600，某些特定的大小会需要加上固定的图片质量参数，就像small和thumb
     * <master|square>控制裁切方式，master是等比例缩放（不裁切）使长度或宽度最大为<size>，square是居中裁切
     * dsc
     * API 内部包含极少量规则，会对某些keyword和tag进行转换，使某些不适合搜索的词变得可以按预期进行搜索，它们通常是一些手游或者人物的缩写或简称，例如
     * <p>
     * vtb => 虚拟YouTuber|VTuber
     * fgo => Fate/GrandOrder|Fate/Grand Order|FateGrandOrder
     * pcr => 公主连结|公主连结Re:Dive|プリンセスコネクト
     * gbf => 碧蓝幻想
     * 舰b => 碧蓝航线|AzurLane
     * 舰c => 舰队collection
     * 少前 => 少女前线|girlsfrontline
     * 将dsc设置为任意真值可以禁用这些转换
     * <p>
     * 响应
     * 字段名	数据类型	说明
     * error	string	错误信息
     * data	setu[]	色图数组
     * setu
     * 字段名	数据类型	说明
     * pid	int	作品 pid
     * p	int	作品所在页
     * uid	int	作者 uid
     * title	string	作品标题
     * author	string	作者名（入库时，并过滤掉 @ 及其后内容）
     * r18	boolean	是否 R18（在库中的分类，不等同于作品本身的 R18 标识）
     * width	int	原图宽度 px
     * height	int	原图高度 px
     * tags	string[]	作品标签，包含标签的中文翻译（有的话）
     * ext	string	图片扩展名
     * uploadDate	int	作品上传日期；时间戳，单位为毫秒
     * urls	object	包含了所有指定size的图片地址
     * 其它细节
     * 自动更新作品信息
     * 被获取的作品如果距离上次更新信息超过一个月，则会被加入更新队列在后台进行信息更新，以防止作品修改或删除造成的原图地址失效。但当次调用以及直到信息更新完毕前，从 API
     * 获取到的信息仍会是旧的信息。
     *
     * @return
     */
    public static List<LoliconData.DataDTO> get(Integer r18, Integer num) {
        String res = HttpRequest.get("https://api.lolicon.app/setu/v2")
                .query("r18", r18)
                .query("num", num)
                .execute()
                .asString();
        LoliconData data = JSON.parseObject(res, LoliconData.class);
        if (StringUtils.isNotEmpty(data.getError())) {
            String pret = JSON.toJSONString(data, true);
            System.out.println(pret);
            return new ArrayList<>();
        }

        List<LoliconData.DataDTO> list = data.getData();
        log.info("get https://api.lolicon.app/setu/v2 success "+ list.size());
        return list;
    }

    @TimeLog
    public static List<LoliconData.DataDTO> get() {
        return get(2, 100);
    }
}
