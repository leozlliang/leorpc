package utils;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Administrator on 2018/6/8.
 */
//@Slf4j
public class ScanPackageUtils {
    private  final static Log log = LogFactory.getLog(ScanPackageUtils.class);
    //扫描  scanPackages 下的文件匹配符
    public static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    /**
     * 找到scanPackages下带注解annotation的全部类信息
     * @param scanPackages 扫包路径,多个路径用","分割
     * @param annotation 注解类
     */
    public static Set<String> findPackageAnnotationClass(String scanPackages, Class<? extends Annotation> annotation)
    {
        if (StringUtils.isEmpty(scanPackages))
        {
            return Sets.newHashSet();
        }

        // 排重包路径，避免父子路径重复扫描
        Set<String> packages = checkPackages(scanPackages);
        //获取Spring资源解析器
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        //创建Spring中用来读取resource为class的工具类
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        Set<String> fullClazzSet = Sets.newHashSet();
        for (String basePackage : packages)
        {
            if (StringUtils.isEmpty(basePackage))
            {
                continue;
            }
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage))
                    + "/" + DEFAULT_RESOURCE_PATTERN;
            try
            {
                //获取packageSearchPath下的Resource，这里得到的Resource是Class信息
                Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
                for (Resource resource : resources)
                {
                    //检查resource，这里的resource都是class
                    String fullClassName = loadClassName(metadataReaderFactory, resource);
                    if (isAnnotationClass(fullClassName, annotation))
                    {
                        fullClazzSet.add(fullClassName);
                    }
                }
            }
            catch (Exception e)
            {
                log.error("获取包下面的类信息失败,package:" + basePackage, e);
            }
        }
        return fullClazzSet;
    }

    /**
     * 排重、检测package父子关系，避免多次扫描
     * @param scanPackages 扫包路径
     * @return 返回全部有效的包路径信息
     */
    private static Set<String> checkPackages(String scanPackages)
    {
        if (StringUtils.isEmpty(scanPackages))
        {
            return Sets.newHashSet();
        }
        Set<String> packages = Sets.newHashSet();
        //排重路径
        Collections.addAll(packages, scanPackages.split(","));
        for (String packageStr : packages.toArray(new String[packages.size()]))
        {
            if (StringUtils.isEmpty(packageStr) || packageStr.equals(".") || packageStr.startsWith("."))
            {
                continue;
            }
            if (packageStr.endsWith("."))
            {
                packageStr = packageStr.substring(0, packageStr.length() - 1);
            }
            Iterator<String> packageIte = packages.iterator();
            boolean needAdd = true;
            while (packageIte.hasNext())
            {
                String pack = packageIte.next();
                if (packageStr.startsWith(pack + "."))
                {
                    //如果待加入的路径是已经加入的pack的子集，不加入
                    needAdd = false;
                }
                else if (pack.startsWith(packageStr + "."))
                {
                    //如果待加入的路径是已经加入的pack的父集，删除已加入的pack
                    packageIte.remove();
                }
            }
            if (needAdd)
            {
                packages.add(packageStr);
            }
        }
        return packages;
    }

    /**
     * 加载资源，根据resource获取className
     * @param metadataReaderFactory spring中用来读取resource为class的工具
     * @param resource 这里的资源就是一个Class
     */
    private static String loadClassName(MetadataReaderFactory metadataReaderFactory, Resource resource)
            throws IOException
    {
        try
        {
            if (resource.isReadable())
            {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                if (metadataReader != null)
                {
                    return metadataReader.getClassMetadata().getClassName();
                }
            }
        }
        catch (Exception e)
        {
            log.error("根据Spring resource获取类名称失败", e);
        }
        return null;
    }

    /**
     * 判断类是否包含特定的注解
     * @param fullClassName
     * @param annotationClass
     * @return
     * @throws ClassNotFoundException
     */
    private static boolean isAnnotationClass(String fullClassName, Class<? extends Annotation> annotationClass)
            throws ClassNotFoundException
    {
        //利用反射，根据类名获取类的全部信息
        Class<?> clazz = Class.forName(fullClassName);
        //获取该类的注解信息
        Annotation annotation = clazz.getAnnotation(annotationClass);
        if (annotation != null)
        {//包含annotationClass注解
            return true;
        }
        return false;
    }

    public static void main(String[] args) {

    }
}