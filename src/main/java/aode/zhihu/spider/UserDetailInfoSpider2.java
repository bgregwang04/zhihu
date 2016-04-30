package aode.zhihu.spider;

import aode.zhihu.extend.QueueNameConstant;
import aode.zhihu.extend.RedisSchedulerExtend2;
import aode.zhihu.model.UserDetailInfo;
import aode.zhihu.pipeline.UserDetailInfoPipeline2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;

@Component
public class UserDetailInfoSpider2 implements Crawl {

	private static final String START_URL = "http://www.zhihu.com/people/excited-vczh";

	@Autowired
	private UserDetailInfoPipeline2 userDetailInfoPipeline2;
	
	private Site site = Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(300).setTimeOut(3 * 60 * 1000)
			.setUserAgent(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31")
                    .setCharset("UTF-8")
		            .addCookie("_xsrf", "cf81e98c4f3b9ea548e9df87a4e5a320")
			        .addCookie("_za", "8d9d4056-20d8-4d22-bdf7-e88443560b4e")
			        .addCookie("z_c0", "QUFDQVBrOGlBQUFYQUFBQVlRSlZUYUQ2dGxiSGozdE42SnNlbzg0U2o2WGk5RDZ2Yi1VWUd3PT0=|1452240288|066af60adedc1989e42f55733738e2738a00c2ed")
			        .addCookie("__utmc", "51854390") ;

	public void crawl() {
		OOSpider.create(site, userDetailInfoPipeline2, UserDetailInfo.class)
		//.setDownloader(new HttpClientDownloaderExtend("/about"))
		.scheduler(new RedisSchedulerExtend2(pool,1, QueueNameConstant.QUEUE_USER_DETAIL_INFO))
		.addUrl(START_URL)
		.thread(4).run();
		
		
	}

	public static void main(String[] args) {
		applicationContext.getBean(UserDetailInfoSpider2.class).crawl();
	}

}
