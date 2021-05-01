package edu.ada.micronaut.service.impl;

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
public class TokenService implements AuthenticationProvider
{
	private static final Logger logger = LoggerFactory.getLogger(TokenService.class);
	
	@Override
	public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest)
	{
		return Flowable.create(emitter ->
		{
			if (authenticationRequest.getIdentity().equals("terlan") &&
					authenticationRequest.getSecret().equals("strongPassword"))
			{
				logger.info("Login successful");
				emitter.onNext(new UserDetails((String) authenticationRequest.getIdentity(), new ArrayList<>()));
				emitter.onComplete();
			}
			else
			{
				emitter.onError(new AuthenticationException(new AuthenticationFailed()));
			}
		}, BackpressureStrategy.ERROR);
	}
}