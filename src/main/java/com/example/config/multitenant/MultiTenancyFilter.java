package com.example.config.multitenant;

import com.example.config.TenantContext;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Component
@Order(1)
public class MultiTenancyFilter implements Filter {
    @Override
    public void doFilter(
    ServletRequest req,
    ServletResponse res,
    FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;

        String tenantId = request.getParameter("TenantId");
        if(tenantId != null)
        {
            request.getSession().setAttribute("TenantId", tenantId);
            System.out.println("Get tenant ID from the query parameter. The ID: " + tenantId);
        }
        else {

            tenantId = (String) request.getSession().getAttribute("TenantId");
            System.out.println("Get tenant ID from the session. The ID: " + tenantId);

        }

        System.out.println("____________________________________________");

        TenantContext.setCurrentTenant(tenantId);
        chain.doFilter(req, res);
    }

}
