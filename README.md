## WebLogic SAML2 extension for Azure Active Directory

# Purpose
This is an extension to Oracle WebLogic 12c+ to support authorization with Microsoft Azure AD Single Sign-On. It allows you to use Azure AD groups, via SAML2 claims, as principals which you match to Java application role(s).

# Why is it needed?
Whilst WebLogic supports SAML2 it appears that Microsoft Azure AD (and ADFS prior to that) does not precisely implement the specification. See [My Oracle Support Note ID 2819226.1](https://support.oracle.com/epmos/faces/DocContentDisplay?id=2819226.1).  Although Azure AD will send a group claim in the SAML2 token, it doesn't include the *NameFormat* and WebLogic ignores it. As a conequence, although you can successfully authenticate in WebLogic using a remote Azure AD user, you cannot then use AD groups (typically the logical way to manage privileges) as the basis of authorization.

# Where could this be useful?
There are several use cases for this where your ID Management has been migrated to Azure AD, such as:
  * WebLogic on-premises (assuming that users have intenet access to Microsoft)
  * WebLogic in OCI (e.g. Marketplace) or another public cloud
  * WebLogic in Kubernetes (though setting up SAML2 is a multi-step process so it's probably only feasible for persistent domains outside the image)

# Credits
This code is very much a "standing on the shoulders of giants" exercise! My primary sources for code were:
* a detailed blog post  http://blog.darwin-it.nl/2014/04/service-provider-initiated-sso-on.html by Martien van den Akker https://github.com/makker-nl,
* CERN SSO code at https://github.com/cerndb/wls-cern-sso, written by Luis Rodríguez Fernandez,
* various similar snippets in small blog posts or forum comments. However, given the similarities in code it was hard to work out the originator.

Without **Martien's blog post** I would not have been so convinced that this was possible, and CERN's implementation is so comprehensive it filled in some of the gaps in my understandng.

A couple of **my colleagues at eProseed** helped with ideas & debugging, especially https://github.com/charlymr to whom I'm very grateful.

# Usage
1. Install into WebLogic & configure SAML2 Identity Provider:
This code needs compiling into a jar file and putting on WebLogic's classpath. 

2. Configure the Enterprise Application in Azure AD:
The SSO part needs to be configured to include the Group claim and its name must be customised to "Group"

I'll provide instructions in a later update, but for now see Martien's post.

# Caveats
It seems to be reliable so far but I'm sure there will be some circumstances, perhaps with more elaborate identity mapping schemes, where the code won't work. If so, WebLogic is very flexible, so I encourage you to take the code and see if you can adapt it to your needs. If you have generic enhancements please raise a PR.  