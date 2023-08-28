package com.ricoandilet.springtools.facade;

import com.ricoandilet.springtools.facade.request.DomainRequest;
import com.ricoandilet.springtools.facade.request.VerifyDomainRequest;
import com.ricoandilet.springtools.facade.response.DomainResponse;
import com.ricoandilet.springtools.facade.response.VerifyDomainResponse;

/**
 * @author: rico
 * @date: 2023/8/24
 **/
public interface DomainFacade {

    VerifyDomainResponse getDomain(DomainRequest domainRequest);

    DomainResponse verifyDomain(VerifyDomainRequest domainRequest);

}
