package org.restexpress.plugin.auth0;

import java.util.Properties;

import org.restexpress.common.exception.ConfigurationException;

/**
 * Supports reading properties for the values set on the Auth0Plugin.
 * The properties file is expected to have values for the following:
 * <ul>
 * <li>auth0.clientId - the Auth0 client ID assigned to your account</li>
 * <li>auth0.secret - the Auth0 secret assigned to your account</li>
 * </ul>
 * A sample configuration looks something like the following:
 * <p/>
 * auth0.clientId = 1676543234567876543456765432<br/>
 * auth0.secret = dfhj76543efghu7654fghji9865rfghjk8765rfbhy65rfvbjuyrf<br/>
 * <p/>
 * All values are required or a ConfigurationException is thrown.
 * 
 * @author toddf
 * @since Nov 16, 2014
 */
public class Auth0Config
{
	private static final String CLIENT_ID_PROPERTY = "cors.clientId";
	private static final String CLIENT_SECRET_PROPERTY = "cors.secret";

	private String clientId;
	private String secret;

	public Auth0Config(Properties p)
	{
		clientId = p.getProperty(CLIENT_ID_PROPERTY);

		if (clientId == null)
		{
			throw new ConfigurationException("Please define Auth0 client ID for property: " + CLIENT_ID_PROPERTY);
		}

		secret = p.getProperty(CLIENT_SECRET_PROPERTY);

		if (secret == null)
		{
			throw new ConfigurationException("Please define Auth0 secret for property: " + CLIENT_SECRET_PROPERTY);
		}
	}

	public String getClientId()
	{
		return clientId;
	}

	public String getSecret()
	{
		return secret;
	}
}
