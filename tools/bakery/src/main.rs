use std::path::PathBuf;

use clap::Parser;
use lumi_bake::BakeInstance;

#[derive(Parser)]
#[clap(version)]
struct Args {
    path: PathBuf,
}

fn main() {
    let args = Args::parse();

    let instance = BakeInstance::new().unwrap();

    let data = lumi_bake::BakedEnvironment::open_from_eq(
        instance.device(),
        instance.queue(),
        &args.path,
        256,
        128,
        Some(1024),
    )
    .unwrap();

    data.save(
        instance.device(),
        instance.queue(),
        &args.path.with_extension("env"),
    )
    .unwrap();
}
