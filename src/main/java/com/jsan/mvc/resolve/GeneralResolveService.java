package com.jsan.mvc.resolve;

import com.jsan.mvc.resolve.suport.ByteResolver;
import com.jsan.mvc.resolve.suport.ForwardResolver;
import com.jsan.mvc.resolve.suport.HtmlResolver;
import com.jsan.mvc.resolve.suport.JsonResolver;
import com.jsan.mvc.resolve.suport.JsonpResolver;
import com.jsan.mvc.resolve.suport.JumpResolver;
import com.jsan.mvc.resolve.suport.RedirectResolver;
import com.jsan.mvc.resolve.suport.StreamResolver;
import com.jsan.mvc.resolve.suport.TextResolver;

public class GeneralResolveService extends AbstractResolveService {

	@Override
	protected void defaultRegisterResolver() {

		registerResolver(new ForwardResolver());
		registerResolver(new HtmlResolver());
		registerResolver(new JsonResolver());
		registerResolver(new JsonpResolver());
		registerResolver(new TextResolver());
		registerResolver(new RedirectResolver());
		registerResolver(new JumpResolver());
		registerResolver(new ByteResolver());
		registerResolver(new StreamResolver());

	}

}
