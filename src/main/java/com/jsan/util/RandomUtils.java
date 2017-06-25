package com.jsan.util;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.UUID;

import javax.xml.bind.DatatypeConverter;

/**
 * 随机数生成工具类。
 *
 */

public class RandomUtils {

	public static final String NUMBER_STRING = "0123456789";
	public static final String ENGLISH_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String WORDCHAR_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	public static final String CHINESE_STRING = "的一是了我不人在他有这个上们来到时大地为子中你说生国年着就那和要她出也得里后自以会家可下而过天去能对小多然于心学么之都好看起发当没成只如事把还用第样道想作种开美总从无情己面最女但现前些所同日手又行意动方期它头经长儿回位分爱老因很给名法间斯知世什两次使身者被高已亲其进此话常与活正感见明问力理尔点文几定本公特做外孩相西果走将月十实向声车全信重三机工物气每并别真打太新比才便夫再书部水像眼等体却加电主界门利海受听表德少克代员许稜先口由死安写性马光白或住难望教命花结乐色更拉东神记处让母父应直字场帄报友关放至张认接告入笑内英军候民岁往何度山觉路带万男边风解叫任金快原吃妈变通师立象数四失满战远格士音轻目条呢病始达深完今提求清王化空业思切怎非找片罗钱紶吗语元喜曾离飞科言干流欢约各即指合反题必该论交终林请医晚制球决窢传画保读运及则房早院量苦火布品近坐产答星精视五连司巴奇管类未朋且婚台夜青北队久乎越观落尽形影红爸百令周吧识步希亚术留市半热送兴造谈容极随演收首根讲整式取照办强石古华諣拿计您装似足双妻尼转诉米称丽客南领节衣站黑刻统断福城故历惊脸选包紧争另建维绝树系伤示愿持千史谁准联妇纪基买志静阿诗独复痛消社算义竟确酒需单治卡幸兰念举仅钟怕共毛句息功官待究跟穿室易游程号居考突皮哪费倒价图具刚脑永歌响商礼细专黄块脚味灵改据般破引食仍存众注笔甚某沉血备习校默务土微娘须试怀料调广蜖苏显赛查密议底列富梦错座参八除跑亮假印设线温虽掉京初养香停际致阳纸李纳验助激够严证帝饭忘趣支春集丈木研班普导顿睡展跳获艺六波察群皇段急庭创区奥器谢弟店否害草排背止组州朝封睛板角况曲馆育忙质河续哥呼若推境遇雨标姐充围案伦护冷警贝著雪索剧啊船险烟依斗值帮汉慢佛肯闻唱沙局伯族低玩资屋击速顾泪洲团圣旁堂兵七露园牛哭旅街劳型烈姑陈莫鱼异抱宝权鲁简态级票怪寻杀律胜份汽右洋范床舞秘午登楼贵吸责例追较职属渐左录丝牙党继托赶章智冲叶胡吉卖坚喝肉遗救修松临藏担戏善卫药悲敢靠伊村戴词森耳差短祖云规窗散迷油旧适乡架恩投弹铁博雷府压超负勒杂醒洗采毫嘴毕九冰既状乱景席珍童顶派素脱农疑练野按犯拍征坏骨余承置臓彩灯巨琴免环姆暗换技翻束增忍餐洛塞缺忆判欧层付阵玛批岛项狗休懂武革良恶恋委拥娜妙探呀营退摇弄桌熟诺宣银势奖宫忽套康供优课鸟喊降夏困刘罪亡鞋健模败伴守挥鲜财孤枪禁恐伙杰迹妹藸遍盖副坦牌江顺秋萨菜划授归浪听凡预奶雄升碃编典袋莱含盛济蒙棋端腿招释介烧误";

	/**
	 * 返回指定大小范围的随机整数值。
	 * 
	 * @param minValue
	 * @param maxValue
	 * @return
	 */
	public static int getInt(int minValue, int maxValue) {

		Random random = new Random();

		int value = maxValue - minValue;
		value = random.nextInt(value + 1);
		value += minValue;

		return value;
	}

	/**
	 * 返回指定长度的随机整数值。
	 * 
	 * @param length
	 * @return
	 */
	public static int getInt(int length) {

		int minValue = (int) Math.pow(10, length - 1);
		if (minValue < 10) {
			minValue = 0;
		}
		int maxValue = ((int) Math.pow(10, length)) - 1;

		return getInt(minValue, maxValue);
	}

	/**
	 * 返回指定长度范围的随机数字。
	 * 
	 * @param minLength
	 * @param maxLength
	 * @return
	 */
	public static String getNumber(int minLength, int maxLength) {

		return getString(NUMBER_STRING, minLength, maxLength);
	}

	/**
	 * 返回指定长度的随机数字。
	 * 
	 * @param length
	 * @return
	 */
	public static String getNumber(int length) {

		return getNumber(length, length);
	}

	/**
	 * 返回指定长度范围的随机汉字。
	 *
	 * @param minLength
	 * @param maxLength
	 * @return
	 */
	public static String getAllChinese(int minLength, int maxLength) {

		int n = 0x9fa5 - 0x4e00 + 1;

		Random random = new Random();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < minLength; i++) {
			sb.append((char) (0x4e00 + random.nextInt(n)));
		}

		int value = maxLength - minLength;

		if (value > 0) {
			value = random.nextInt(value + 1);
			for (int i = 0; i < value; i++) {
				sb.append((char) (0x4e00 + random.nextInt(n)));
			}
		}

		return sb.toString();
	}

	/**
	 * 返回指定长度的随机汉字。
	 *
	 * @param length
	 * @return
	 */
	public static String getAllChinese(int length) {

		return getAllChinese(length, length);
	}

	/**
	 * 返回指定长度范围的随机汉字（1000个常用汉字）。
	 * 
	 * @param minLength
	 * @param maxLength
	 * @return
	 */
	public static String getChinese(int minLength, int maxLength) {

		return getString(CHINESE_STRING, minLength, maxLength);
	}

	/**
	 * 返回指定长度的随机汉字（1000个常用汉字）。
	 * 
	 * @param length
	 * @return
	 */
	public static String getChinese(int length) {

		return getChinese(length, length);
	}

	/**
	 * 返回指定长度范围的随机字符。
	 * 
	 * @param str
	 *            指定的待选字符串
	 * @param minLength
	 * @param maxLength
	 * @return
	 */
	public static String getString(String str, int minLength, int maxLength) {

		if (str != null) {

			char[] charArray = str.toCharArray();
			int charLength = charArray.length;

			Random random = new Random();
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < minLength; i++) {
				sb.append(charArray[random.nextInt(charLength)]);
			}

			int value = maxLength - minLength;

			if (value > 0) {
				value = random.nextInt(value + 1);
				for (int i = 0; i < value; i++) {
					sb.append(charArray[random.nextInt(charLength)]);
				}
			}

			return sb.toString();

		} else {
			return "";
		}
	}

	/**
	 * 返回指定长度的随机字符。
	 * 
	 * @param str
	 *            指定的待选字符串
	 * @param length
	 * @return
	 */
	public static String getString(String str, int length) {

		return getString(str, length, length);
	}

	/**
	 * 返回指定长度范围的随机单词字符。
	 * <p>
	 * 字符范围："abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
	 * 
	 * @param minLength
	 * @param maxLength
	 * @return
	 */
	public static String getWordChar(int minLength, int maxLength) {

		return getString(WORDCHAR_STRING, minLength, maxLength);
	}

	/**
	 * 返回指定长度的随机单词字符。
	 * <p>
	 * 字符范围："abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
	 * 
	 * @param length
	 * @return
	 */
	public static String getWordChar(int length) {

		return getWordChar(length, length);
	}

	/**
	 * 返回指定长度范围的随机英文字符。
	 * <p>
	 * 字符范围："abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
	 * 
	 * @param minLength
	 * @param maxLength
	 * @return
	 */
	public static String getEnglish(int minLength, int maxLength) {

		return getString(ENGLISH_STRING, minLength, maxLength);
	}

	/**
	 * 返回指定长度的随机英文字符。
	 * <p>
	 * 字符范围："abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
	 * 
	 * @param length
	 * @return
	 */
	public static String getEnglish(int length) {

		return getEnglish(length, length);
	}

	/**
	 * 返回 UUID 随机字符串。
	 * 
	 * @return
	 */
	public static String getUUID() {

		return UUID.randomUUID().toString();
	}

	/**
	 * 返回 UUID 随机数字符串，并去除中横杆符号（-）。
	 * 
	 * @return
	 */
	public static String getUUIDUnsigned() {

		return getUUID().replaceAll("-", "");
	}

	/**
	 * 返回指定长度（不低于11）的随机序列码（基本不重复，长度越大越无重复的可能）。
	 * 
	 * @param length
	 * @return
	 */
	public static String getSequenceCode(int length) {

		String str = parseCurrentTimeMillisToSequenceCode();

		while (str.indexOf('/') > -1 || str.indexOf('+') > -1) {
			str = parseCurrentTimeMillisToSequenceCode();
		}
		if (length > 11) {
			int len = length - 11;
			str = getWordChar(len) + str;
		}

		return str;
	}

	/**
	 * 将当前时间值转换为序列码（System.currentTimeMillis 乘以一个随机整数后再进行 Base64 编码）。
	 * 
	 * @return
	 */
	private static String parseCurrentTimeMillisToSequenceCode() {

		long l = System.currentTimeMillis();
		int i = getInt(1, 999999);
		l *= i;
		byte[] buffer = parseLongToByte(l);
		String str = parseBase64EncodeWithoutEqualsSign(buffer);

		return str;
	}

	/**
	 * 将 byte[] 进行 Base64 编码，并去除填补符号（=）。
	 * 
	 * @param bytes
	 * @return
	 */
	private static String parseBase64EncodeWithoutEqualsSign(byte[] bytes) {

		String str = null;

		if (bytes != null) {
			str = DatatypeConverter.printBase64Binary(bytes);
			int index = str.indexOf('=');
			if (index > -1) {
				str = str.substring(0, index);
			}
		}

		return str;
	}

	/**
	 * 将 long 转换为 byte[] 。
	 * 
	 * @param l
	 * @return
	 */
	private static byte[] parseLongToByte(long l) {

		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putLong(0, l);
		return buffer.array();
	}

}
