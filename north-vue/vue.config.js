module.exports = {
    pluginOptions: {
        'style-resources-loader': {
            preProcessor: 'less',
            patterns: []
        }
    },
    configureWebpack: {
        // devtool
        devtool: 'cheap-module-eval-source-map'
    },
    lintOnSave: false,
    productionSourceMap: false,
    publicPath: './',
    runtimeCompiler: true
}
