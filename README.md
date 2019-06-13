平台的规则微服务

功能：
input: 实时的一条完整设备信息。 output: 发送给plugin的一条包含json的http。

function: 大致义务，每一规则有自己的filters[多个], processor[一个], 启动包含了处理plugin的相关参数、地址和附加数据。 每一条规则只要filter生效，该ruler即生效。

开发：
在依赖上所有的plugin的开发依赖default_plugin[default_plugin 是plugin与smart_ruler的信息交互包， 重点：共有]，
1、在开发上，必须先把default_plugin通过maven install安装到开发环境里。
2、通过实现一个abstract 服务class来支持对smart_ruler提供服务。
3、在开发时，所有的三级包目录保持唯一性【不同】。
