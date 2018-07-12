<#include "header.ftl">
    <div id="main">
        <div class="container">
            <ul class="letter-chatlist">
                <#list messages as msg>
                <li id="msg-item-4009580">
                    <a class="list-head">
                        <img alt="头像" src="${msg.headUrl!}">
                    </a>
                    <div class="tooltip fade right in">
                        <div class="tooltip-arrow"></div>
                        <div class="tooltip-inner letter-chat clearfix">
                            <div class="letter-info">
                                <p class="letter-time"> ${msg.message.createdDate?string('yyyy-MM-dd HH:mm:ss')}</p>
                                <a href="javascript:void(0);" id="del-link" name="4009580">删除</a>
                            </div>
                            <p class="chat-content">
                                ${msg.message.content!}
                            </p>
                        </div>
                    </div>
                </li>
                </#list>
</ul>

        </div>
        <script type="text/javascript">
          $(function(){

            // If really is weixin
            $(document).on('WeixinJSBridgeReady', function() {

              $('.weixin-qrcode-dropdown').show();

              var options = {
                "img_url": "",
                "link": "http://nowcoder.com/j/wt2rwy",
                "desc": "",
                "title": "读《Web 全栈工程师的自我修养》"
              };

              WeixinJSBridge.on('menu:share:appmessage', function (argv){
                WeixinJSBridge.invoke('sendAppMessage', options, function (res) {
                  // _report('send_msg', res.err_msg)
                });
              });

              WeixinJSBridge.on('menu:share:timeline', function (argv) {
                WeixinJSBridge.invoke('shareTimeline', options, function (res) {
                  // _report('send_msg', res.err_msg)
                });
              });

               $(window).on('touchmove scroll', function() {
                 if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
                   $('div.backdrop').show();
                   $('div.share-help').show();
                 } else {
                   $('div.backdrop').hide();
                   $('div.share-help').hide();
                 }
               });

            });

          })
        </script>
    </div>
<#include "footer.ftl">