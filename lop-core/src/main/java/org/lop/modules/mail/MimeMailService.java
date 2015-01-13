package org.lop.modules.mail;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.google.common.collect.Sets;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * MIME邮件服务类.
 * 
 * @author 潘瑞峥
 * @date 2014年11月28日
 */
public class MimeMailService {

	private static final Logger LOG = LoggerFactory.getLogger( MimeMailService.class );

	/** 发件人邮箱. */
	private String from;

	/** 发件人昵称. */
	private String nickname;

	private JavaMailSender mailSender;

	private Template template;

	/**
	 * 发送纯文本邮件.
	 */
	public void sendTextMail( String[] to, String subject, String content, File... attachments ) throws Exception {
		this.sendMail( to, subject, content, Boolean.FALSE, attachments );
	}

	/**
	 * 发送纯文本邮件.
	 */
	public void sendTextMail( String[] to, String subject, String content, Collection<File> attachments ) throws Exception {
		this.sendMail( to, subject, content, Boolean.FALSE, attachments );
	}

	/**
	 * 发送HTML邮件.
	 */
	public void sendHtmlMail( String[] to, String subject, String content, File... attachments ) throws Exception {
		this.sendMail( to, subject, content, Boolean.TRUE, attachments );
	}

	/**
	 * 发送HTML邮件.
	 */
	public void sendHtmlMail( String[] to, String subject, String content, Collection<File> attachments ) throws Exception {
		this.sendMail( to, subject, content, Boolean.TRUE, attachments );
	}

	/**
	 * 发送HTML邮件.
	 */
	private void sendMail( String[] to, String subject, String content, boolean html, File... attachments ) throws Exception {
		this.sendMail( to, subject, content, html, Sets.newHashSet( attachments ) );
	}

	/**
	 * 发送邮件.
	 */
	private void sendMail( String[] to, String subject, String content, boolean html, Collection<File> attachments ) throws Exception {
		Validate.notEmpty( to, "收件人地址不能为空" );
		Validate.notBlank( subject, "邮件主题不能为空" );
		Validate.notBlank( content, "邮件内容不能为空" );

		boolean multipart = false;
		// 有附件.
		if ( CollectionUtils.isNotEmpty( attachments ) ) {
			multipart = true;
		}

		MimeMessage message = this.mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper( message, multipart, CharEncoding.UTF_8 );

		helper.setFrom( from, nickname );
		helper.setTo( to );
		helper.setSubject( subject );
		helper.setText( content, html );

		// 附件.
		if ( CollectionUtils.isNotEmpty( attachments ) ) {
			for ( File attachment : attachments ) {
				helper.addAttachment( attachment.getName(), attachment );
			}
		}

		mailSender.send( message );

		LOG.debug( "{}<{}>邮件已发送至{}", nickname, from, StringUtils.join( to, "," ) );
	}

	/**
	 * 使用Freemarker生成html格式内容.
	 */
	@SuppressWarnings( "unused" )
	private String generateContent( String userName ) throws MessagingException {

		try {
			Map<String, String> context = Collections.singletonMap( "userName", userName );
			return FreeMarkerTemplateUtils.processTemplateIntoString( template, context );
		} catch ( IOException e ) {
			LOG.error( "生成邮件内容失败, FreeMarker模板不存在", e );
			throw new MessagingException( "FreeMarker模板不存在", e );
		} catch ( TemplateException e ) {
			LOG.error( "生成邮件内容失败, FreeMarker处理失败", e );
			throw new MessagingException( "FreeMarker处理失败", e );
		}
	}

	/**
	 * 注入Freemarker引擎配置, 构造Freemarker邮件内容模板.
	 */
	public void setFreemarkerConfiguration( Configuration freemarkerConfiguration ) throws IOException {
		// 根据freemarkerConfiguration的templateLoaderPath载入文件.
		template = freemarkerConfiguration.getTemplate( "mailTemplate.ftl", CharEncoding.UTF_8 );
	}

	public void setFrom( String from ) {
		this.from = from;
	}

	public void setNickname( String nickname ) {
		this.nickname = nickname;
	}

	public void setMailSender( JavaMailSender mailSender ) {
		this.mailSender = mailSender;
	}

}