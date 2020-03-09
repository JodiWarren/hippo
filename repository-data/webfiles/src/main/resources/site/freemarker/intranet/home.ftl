<#ftl output_format="HTML">
<#include "../include/imports.ftl">

<#assign documentTitle = "Hello world!" />

<article class="article article--intranet-home">
    <div class="grid-wrapper grid-wrapper--article">
        <div class="article-header">
            <h1 data-uipath="ps.document.title">${documentTitle}</h1>
        </div>

        <div class="grid-row">
            <div class="column column--two-thirds page-block page-block--main">
                <div class="article-section">
                    <h2>Just a section</h2>
                    <p>Synergistically incubate B2B technologies vis-a-vis long-term high-impact human capital. Objectively drive ethical infomediaries via e-business models. Dynamically enhance emerging e-commerce for user-centric synergy. Progressively architect customer directed e-services before empowered potentialities. Efficiently innovate B2C resources and clicks-and-mortar action items.</p>
                    <p>Distinctively benchmark vertical methods of empowerment through seamless core competencies. Objectively plagiarize e-business technology via multidisciplinary action.</p>
                </div>
                <div class="article-section">
                    <#if hstRequest.userPrincipal??>

                        <p>Welcome <strong>${hstRequest.userPrincipal.name}</strong>!</p>
                        <form name="logoutForm" method="post" action="<@hst.link path="/logout"/>">
                            <#if _csrf??>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            </#if>
                            <input type="submit" name="logoutButton" value="Log out" />
                        </form>
                    </#if>
                </div>
            </div>
        </div>
    </div>
</article>
