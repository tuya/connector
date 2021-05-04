package com.tuya.connector.spring.export;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/20 5:37 下午
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExportConfiguration {
    boolean autoExport;
}
