package com.eproseed.wlssaml2;
// Tested with WebLogic 12.2.1.3 & 12.2.1.4

import com.bea.security.saml2.providers.SAML2AttributeInfo;
import com.bea.security.saml2.providers.SAML2AttributeStatementInfo;
import com.bea.security.saml2.providers.SAML2IdentityAsserterAttributeMapper;
import com.bea.security.saml2.providers.SAML2IdentityAsserterNameMapper;
import com.bea.security.saml2.providers.SAML2NameMapperInfo;
 
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

import weblogic.security.service.ContextHandler;
 
public class AzureAdSsoSaml2IdentityMapper implements SAML2IdentityAsserterNameMapper,
                                                      SAML2IdentityAsserterAttributeMapper {

  // you need both methods, i.e. if you don't override the SAML2...NameMapper WebLogic seems to ignore the library
  public AzureAdSsoSaml2IdentityMapper() {
		super();
	}
 
	@Override
	public String mapNameInfo(SAML2NameMapperInfo mapperInfo,
			                      ContextHandler contextHandler) {
		return mapperInfo.getName();
  }

  // Map from collection of SAMLAttributeStatementInfo to collection of Principal(s)
  // This collection of Principals is added to the context handler under name ContextElementDictionary.SAML_ATTRIBUTE_PRINCIPALS

  @Override 
  public Collection<Principal> mapAttributeInfo(Collection<SAML2AttributeStatementInfo> attrStmtInfos,
                                                ContextHandler contextHandler) {
    // Note: WebLogic calls this method BEFORE mapNameInfo

    Collection<Principal> principals = new ArrayList<Principal>();
    if (attrStmtInfos == null || attrStmtInfos.size() == 0) {
    } else {
      for (SAML2AttributeStatementInfo stmtInfo : attrStmtInfos) {

        Collection<SAML2AttributeInfo> attrs = stmtInfo.getAttributeInfo();   // stmtInfo is each Attr in turn
        if (attrs == null || attrs.size() == 0) {
        } else {
          for (SAML2AttributeInfo attr : attrs) { // 10 attributes

            // Note that the User Principal mapping is done outside of this attribute mapping and, if you can use email as the user principal, works for Azure SSO without any changes

            // By default Azure AD SAML sends Group claim name as http://schemas.microsoft.com/ws/2008/06/identity/claims/groups
            // but Azure allows you to customise, so configure "Groups"at the Azure end for better isolation
 
            if ( attr.getAttributeName().equals("Groups") ) {
              // https://docs.oracle.com/middleware/12213/wls/WLAPI/com/bea/security/saml2/providers/SAML2AttributeInfo.html
              // set the namespace, which Azure doesn't, as per SAML2 spec
              attr.setAttributeNameFormat("urn:oasis:names:tc:SAML:2.0:attrname-format:basic"); // if possible use the WebLogic static which it is testing for (see decompiled code)

              for (String attributeValue : attr.getAttributeValues()) { // loop through all the user's Groups in Group values collection
                if (attributeValue.length()>1) {
                  CustomPrincipal groupPrincipal = new CustomPrincipal(attributeValue);
                  principals.add(groupPrincipal);
                }
              }
            }
          }
        }
      }
    }
    return principals;
  } 
}