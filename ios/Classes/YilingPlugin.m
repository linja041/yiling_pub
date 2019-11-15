#import "YilingPlugin.h"
#import <yiling_plugin/yiling_plugin-Swift.h>

@implementation YilingPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftYilingPlugin registerWithRegistrar:registrar];
}
@end
